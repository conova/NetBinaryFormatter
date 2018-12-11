
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *
 * @author conova
 */
public class Functions {

    public static byte[] chrstob(String chrs, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < Math.min(len, chrs.getBytes(StandardCharsets.UTF_8).length); i++) {
            b[i] = chrs.getBytes(StandardCharsets.UTF_8)[i];
        }
        return b;
    }

    public static byte[] itob(int value) {
        return new byte[]{
            (byte) value,
            (byte) (value >>> 8),
            (byte) (value >>> 16),
            (byte) (value >>> 24)
        };
    }

    public static byte[] ftob(float f) {
        int value = Float.floatToIntBits(f);
        return itob(value);
    }




    public static byte[] strtob(String s) {
        return s.getBytes();
    }
    
    public static byte booltob(boolean bool){
        if(bool == true ) return 0x01;
        return 0x00;
    }
    
    public static boolean btobool(byte b){
        if(b == 0x00) return false;
        return true;
    }

    public static String btostr(byte[] b) {
        return new String(b);
    }

    public static int btoi(byte[] bytes) {
        if (bytes.length < 4) {
            if (bytes.length == 0) {
                return 0;
            }
            if (bytes.length == 1) {
                return (bytes[0] & 0xFF);
            }
            if (bytes.length == 2) {
                return (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
            }
            if (bytes.length == 3) {
                return (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
            }
        }
        return bytes[3] << 24 | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }

    public static double btod(byte[] bytes) {
        long l = btol(bytes);
        return Double.longBitsToDouble(l);
    }

    public static double btof(byte[] bytes) {
        int l = btoi(bytes);
        return Float.intBitsToFloat(l);
    }
    
    public static long btol(byte[] bytes){
        return bytes[7] << 56 | (bytes[6] & 0xFF) << 48 | (bytes[5] & 0xFF) << 40 | (bytes[4] <<32) | bytes[3] << 24 | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }

    public static byte[] stob(short value) {
        return new byte[]{
            (byte) (value),
            (byte) (value >>> 8)};
    }

    public static byte[] dtob(double value) {
        long l = Double.doubleToRawLongBits(value);
        return ltob(l);
    }

    public static byte[] ltob(long l) {
        return new byte[]{
            (byte) l,
            (byte) (l >>> 8),
            (byte) (l >>> 16),
            (byte) (l >>> 24),
            (byte) (l >>> 32),
            (byte) (l >>> 40),
            (byte) (l >>> 48),
            (byte) (l >>> 56)
        };
    }
    
    public static byte[] merge(byte[]... b) throws IOException {
        if(b.length < 1) return new byte[0];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < b.length; i++) {
            bos.write(b[i]);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static byte[] hexToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 3];
        for (int i = 0; i < len; i += 3) {
            data[i / 3] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byteArrayToHex(byte[] a) {
        if (a == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString();
    }

    public static byte[] range(byte[] resp, int from, int to) {
        if (resp.length >= to && to >= from) {
            return Arrays.copyOfRange(resp, from, to);
        }
        return new byte[Math.abs(to - from)];
    }

    public static byte[] range(byte[] resp, int from) {
        return range(resp, from, resp.length);
    }
}
