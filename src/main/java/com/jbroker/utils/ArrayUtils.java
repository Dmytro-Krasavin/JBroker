package com.jbroker.utils;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // This is the utility class
public class ArrayUtils {

  public static byte[] mergeArrays(byte[]... arrays) {
    int totalLength = Arrays.stream(arrays)
        .mapToInt(array -> array.length)
        .sum();

    byte[] resultArray = new byte[totalLength];
    int resultArrayIndex = 0;
    for (byte[] array : arrays) {
      for (byte element : array) {
        resultArray[resultArrayIndex] = element;
        resultArrayIndex++;
      }
    }
    return resultArray;
  }

  public static byte[] toByteArray(List<Byte> byteList) {
    byte[] byteArray = new byte[byteList.size()];
    for (int i = 0; i < byteList.size(); i++) {
      byteArray[i] = byteList.get(i);
    }
    return byteArray;
  }
}
