package org.lhq.service.utils;

import java.util.Collection;

public class CommonUtils {
    public static byte[]  byteArrayTran(Collection<Byte> byteList) {
        byte[] byteData = new byte[byteList.size()];
        int index = 0;
        for (Byte aByte : byteList) {
            byteData[index++] = aByte;
        }
        return byteData;
    }
}
