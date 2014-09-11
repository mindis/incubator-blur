/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.blur.manager.command;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class BlurObject {

  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

  private Map<String, Object> _valueMap = new TreeMap<String, Object>();

  public BlurObject() {

  }

  public BlurObject(BlurObject object) {
    _valueMap.putAll(object._valueMap);
  }

  public void accumulate(String name, String value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, String value) {
    put(name, (Object) value);
  }

  public String getString(String name) {
    return (String) _valueMap.get(name);
  }

  public void accumulate(String name, Integer value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Integer value) {
    put(name, (Object) value);
  }

  public Integer getInteger(String name) {
    return (Integer) _valueMap.get(name);
  }

  public void accumulate(String name, Short value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Short value) {
    put(name, (Object) value);
  }

  public Short getShort(String name) {
    return (Short) _valueMap.get(name);
  }

  public void accumulate(String name, Long value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Long value) {
    put(name, (Object) value);
  }

  public Long getLong(String name) {
    return (Long) _valueMap.get(name);
  }

  public void accumulate(String name, Double value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Double value) {
    put(name, (Object) value);
  }

  public Double getDouble(String name) {
    return (Double) _valueMap.get(name);
  }

  public void accumulate(String name, Float value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Float value) {
    put(name, (Object) value);
  }

  public Float getFloat(String name) {
    return (Float) _valueMap.get(name);
  }

  public void accumulate(String name, byte[] value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, byte[] value) {
    put(name, (Object) value);
  }

  public byte[] getBinary(String name) {
    return (byte[]) _valueMap.get(name);
  }

  public void accumulate(String name, Boolean value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, Boolean value) {
    put(name, (Object) value);
  }

  public Boolean getBoolean(String name) {
    return (Boolean) _valueMap.get(name);
  }

  public void accumulate(String name, BlurObject value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, BlurObject value) {
    put(name, (Object) value);
  }

  public BlurObject getBlurObject(String name) {
    return (BlurObject) _valueMap.get(name);
  }

  public void accumulate(String name, BlurArray value) {
    accumulate(name, (Object) value);
  }

  public void put(String name, BlurArray value) {
    put(name, (Object) value);
  }

  public BlurArray getBlurArray(String name) {
    return (BlurArray) _valueMap.get(name);
  }

  public void accumulate(String name, Object value) {
    checkType(value);
    Object object = _valueMap.get(name);
    if (object == null) {
      _valueMap.put(name, value);
    } else {
      if (object instanceof BlurArray) {
        BlurArray array = (BlurArray) object;
        array.put(value);
      } else {
        BlurArray array = new BlurArray();
        array.put(object);
        array.put(value);
        _valueMap.put(name, array);
      }
    }
  }

  public static void checkType(Object value) {

  }

  public void put(String name, Object value) {
    checkType(value);
    _valueMap.put(name, value);
  }

  @Override
  public String toString() {
    return toString(0);
  }

  public String toString(int i) {
    StringBuilder builder = new StringBuilder();
    builder.append('{');
    boolean comma = false;
    for (Entry<String, Object> e : _valueMap.entrySet()) {
      if (comma) {
        builder.append(',');
      }
      comma = true;
      if (i > 0) {
        builder.append('\n');
        for (int j = 0; j < i; j++) {
          builder.append(' ');
        }
      }
      builder.append(stringify(e.getKey()));
      builder.append(':');
      Object value = e.getValue();
      if (value instanceof BlurObject) {
        builder.append(((BlurObject) value).toString(i > 0 ? i + 1 : 0));
      } else if (value instanceof BlurArray) {
        builder.append(((BlurArray) value).toString(i > 0 ? i + 1 : 0));
      } else {
        builder.append(stringify(value));
      }
    }
    if (i > 0) {
      builder.append('\n');
      for (int j = 0; j < i - 1; j++) {
        builder.append(' ');
      }
    }
    builder.append('}');
    return builder.toString();
  }

  public static String toHexString(byte[] bs) {
    char[] hexChars = new char[bs.length * 2];
    for (int j = 0; j < bs.length; j++) {
      int v = bs[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static Object stringify(Object o) {
    if (o instanceof Number) {
      return o.toString();
    } else if (o instanceof byte[]) {
      return toHexString((byte[]) o);
    } else if (o instanceof Boolean) {
      return o.toString();
    } else if (o instanceof String) {
      return "\"" + o.toString() + "\"";
    } else {
      throw new RuntimeException("Cannot stringify object [" + o + "]");
    }
  }

  public Iterator<String> keys() {
    return _valueMap.keySet().iterator();
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String name) {
    return (T) _valueMap.get(name);
  }

  public Object getObject(String name) {
    return _valueMap.get(name);
  }

}