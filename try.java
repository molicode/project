======= try this 
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class CsvService {

    public <T> String generateCsv(List<T> list, Class<T> type) {
        try (StringWriter writer = new StringWriter()) {
            ColumnPositionMappingStrategy<T> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(type);

            // Define las cabeceras a partir de los nombres de los campos de la clase T
            // Esto requiere un método adicional para obtener los nombres de los campos

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            beanToCsv.write(list);
            return writer.toString();
        } catch (Exception e) {
            // Manejar la excepción como sea necesario
            return null;
        }
    }
}


========== test

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class CsvControllerTest {

    @Mock
    private CsvService csvService;

    @InjectMocks
    private CsvController csvController;

    @Test
    public void testGenerateCsv() {
        List<MyObject> myList = Arrays.asList(new MyObject(), new MyObject());
        String expectedCsv = "id,name\n1,Example\n2,Example2";
        
        when(csvService.generateCsv(anyList(), eq(MyObject.class))).thenReturn(expectedCsv);
        
        ResponseEntity<String> response = csvController.generateCsv(myList);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCsv, response.getBody());

        verify(csvService).generateCsv(anyList(), eq(MyObject.class));
    }
}
