package br.com.jamalxvi.quake.parser.util;

import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapUtilTest {
  @Test
  void sortByValue_AllValid_Sucess() {
    HashMap<String, Integer> valores = new HashMap<>();
    valores.put("d", 30);
    valores.put("a", 60);
    valores.put("z", 1);
    Map<String, Integer> valoresOrdenados = MapUtil.sortByValue(valores);
      Object[] chaves = valoresOrdenados.keySet().toArray();
    assertTrue("O primeiro valor deve ser o Z", chaves[0].equals("z"));
    assertTrue("O segundo valor deve ser o D", chaves[1].equals("d"));
    assertTrue("O tercei valor deve ser o A", chaves[2].equals("a"));
  }
}
