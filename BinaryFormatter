
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author conova
 */
public class BinaryFormatter {

    public static final byte BOOLEAN = 0x01;
    public static final byte BYTE = 0x02;
    public static final byte CHAR = 0x03;
    public static final byte DOUBLE = 0x06;
    public static final byte SHORT = 0x07;
    public static final byte INTEGER = 0x08;
    public static final byte LONG = 0x09;
    public static final byte FLOAT = 0x0b;
    public static final byte STRING = 0x66;
    public static final byte STRLEN = 0x61;
    public static final byte COPY = 0x69;

    public static final byte BASIC_OBJECT = 0x08;
    public static final byte STRING_OBJECT = 0x06;
    public static final byte COPY_OBJECT = 0x09;
    public static final byte BASIC_ARRAY_OBJECT = 0x0f;
    public static final byte STRING_ARRAY_OBJECT = 0x11;

    Object[] obj;
    byte[] b;
    int strIndex;
    int start;
    HashMap<Object, Integer> datamap;
    HashMap<Integer, Object> keymap;
    List<Mapper> mapper;

    public BinaryFormatter() {
    }

    public BinaryFormatter(Object[] obj) {
        this.obj = obj;
    }

    private byte[] format() throws IOException {
        strIndex = 2;
        datamap = new HashMap<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(frontByte());
        bos.write(Functions.itob(1));
        bos.write(Functions.itob(obj.length));
        for (int i = 0; i < obj.length; i++) {
            bos.write(netBinary(obj[i]));
        }
        bos.write(endByte());
        b = bos.toByteArray();
        bos.close();
        return b;
    }

    public byte[] format(Object[] obj) throws IOException {
        this.obj = obj;
        return format();
    }

    public Object[] parse(byte[] b) {
        if(b.length ==  0) return new  Object[0];
        this.b = b;
        start = 18;
        keymap = new HashMap<>();
        mapper = new ArrayList<>();
        ByteArrayInputStream in = new ByteArrayInputStream(b, 0, b.length);
        int h = (int) parseOne(INTEGER);
        int w = (int) parseOne(INTEGER);
        obj = new Object[w];
        List<Object> objects = new ArrayList<>();
        int l;
        byte tp;
        for (int ii = 0; ii < w; ii++) {
            tp = (byte) parseOne(BYTE);
            if (tp == BASIC_OBJECT) {
                obj[ii] = parseOne((byte) parseOne(BYTE));
            } else if (tp == STRING_OBJECT) {
                strIndex = (int) parseOne(INTEGER);
                l = (int) parseOne(STRLEN);
                obj[ii] = parseOne(STRING, l);
                keymap.put(strIndex, obj[ii]);
            } else if (tp == COPY_OBJECT) {
                obj[ii] = parseOne(COPY);
                if (obj[ii] instanceof Integer) {
                    mapper.add(new Mapper((int) obj[ii], ii));
                }
            }
        }
        while (0x0b != (tp = (byte) parseOne(BYTE)) || start < obj.length) {
            if (tp == BASIC_ARRAY_OBJECT || tp == STRING_ARRAY_OBJECT) {
                l = (int) parseOne(INTEGER);
                int ww = (int) parseOne(INTEGER);
                tp = (byte) parseOne(BYTE);
                Object array[] = new Object[ww];
                for (int ii = 0; ii < ww; ii++) {
                    if (tp == BASIC_OBJECT) {
                        array[ii] = parseOne(tp);
                    } else if (tp == STRING_OBJECT) {
                        strIndex = (int) parseOne(INTEGER);
                        l = (int) parseOne(STRLEN);
                        array[ii] = parseOne(STRING, l);
                        keymap.put(strIndex, array[ii]);
                    }
                }
                keymap.put(l, array);
            }
        }

        for (Mapper m : mapper) {
            if (keymap.containsKey(m.ind)) {
                obj[m.val] = keymap.get(m.ind);
            }
        }

        return obj;
    }

    class Mapper {

        int ind;
        int val;

        public Mapper(int ind, int val) {
            this.ind = ind;
            this.val = val;
        }
    }

    public Object parseOne(byte type) {
        return parseOne(type, 0);
    }

    public Object parseOne(byte type, int len) {
        switch (type) {
            case STRLEN: {
                return netStringLenToInt();
            }
            case STRING: {
                return typeObject(type, len);
            }
            case COPY: {
                return typeObject(type);
            }
            default:
                return typeObject(type);
        }
    }

    public int typeLen(byte type) {
        switch (type) {
            case BOOLEAN:
                return 1;
            case INTEGER:
                return 4;
            case CHAR:
                return 1;
            case FLOAT:
                return 4;
            case DOUBLE:
                return 8;
            case BYTE:
                return 1;
            case LONG:
                return 8;
        }
        return 1;
    }

    public Object typeObject(byte type) {
        return typeObject(type, typeLen(type));
    }

    private byte[] ranges(int len) {
        start += len;
        return Functions.range(b, start - len, start);
    }

    public Object typeObject(byte type, int len) {
        switch (type) {
            case BOOLEAN:
                return Functions.btobool(ranges(len)[0]);
            case INTEGER:
                return Functions.btoi(ranges(len));
            case CHAR:
                return (char) (byte) ranges(len)[0];
            case FLOAT:
                return Functions.btof(ranges(len));
            case DOUBLE:
                return Functions.btod(ranges(len));
            case BYTE:
                return ranges(len)[0];
            case LONG:
                return Functions.btol(ranges(len));
            case STRLEN:
                return netStringLenToInt(ranges(len));
            case STRING:
                return Functions.btostr(ranges(len));
            case COPY:
                int ind = (int) typeObject(INTEGER);
                if (keymap.containsKey(ind)) {
                    return keymap.get(ind);
                } else {
                    return ind;
                }
        }
        return 1;
    }

    public static byte[] netStringToByte(String s) throws IOException {
        byte b[] = Functions.strtob(s);
        netStringLenToByte(b.length);
        return Functions.merge(netStringLenToByte(b.length), b);
    }

    public byte[] netBinary(Object o) throws IOException {
        if (o instanceof Byte) {
            return new byte[]{BASIC_OBJECT, BYTE, (byte) o};
        }
        if (o instanceof Character) {
            return new byte[]{BASIC_OBJECT, CHAR, (byte) (char) o};
        }
        if (o instanceof Boolean) {
            return new byte[]{BASIC_OBJECT, BOOLEAN, Functions.booltob((boolean) o)};
        }
        if (o instanceof Short) {
            return Functions.merge(new byte[]{BASIC_OBJECT, SHORT}, Functions.stob((short) o));
        }
        if (o instanceof Integer) {
            return Functions.merge(new byte[]{BASIC_OBJECT, INTEGER}, Functions.itob((int) o));
        }
        if (o instanceof Float) {
            return Functions.merge(new byte[]{BASIC_OBJECT, FLOAT}, Functions.ftob((float) o));
        }
        if (o instanceof Long) {
            return Functions.merge(new byte[]{BASIC_OBJECT, LONG}, Functions.ltob((long) o));
        }
        if (o instanceof Double) {
            return Functions.merge(new byte[]{BASIC_OBJECT, DOUBLE}, Functions.dtob((double) o));
        }
        if (o instanceof String) {
            if (datamap.containsKey(o)) {
                return Functions.merge(new byte[]{COPY_OBJECT}, Functions.itob(datamap.get(o)));
            } else {
                datamap.put(o, strIndex);
                return Functions.merge(new byte[]{STRING_OBJECT}, netStrIndex(), netStringToByte((String) o));
            }
        }
        return new byte[0];
    }

    private byte[] netStrIndex() {
        return Functions.itob(strIndex++);
    }

    public int netStringLenToInt() {
        for (int i = start; i < b.length; i++) {
            if ((b[i] & 0xFF) < (0x80 & 0xFF)) {
                return netStringLenToInt(ranges(i - start + 1));
            }
        }
        return 0;
    }

    public static int netStringLenToInt(byte[] b) {
        int i = 0;
        int res = 0;
        int ss = 1;
        while (i < b.length && (b[i] & 0xFF) >= (0x80 & 0xFF)) {
            res += (b[i] & 0xFF - 128) * ss;
            i++;
            ss *= 128;
        }
        if (i < b.length) {
            res += ((b[i] & 0xFF)) * ss;
        }
        return res;
    }

    public static byte[] netStringLenToByte(int len) {
        if( len ==0 ) return new byte[]{0x00};
        byte[] b = new byte[10];
        int i = 0;
        while (len != 0) {
            b[i] = (byte) (len % 128);
            len /= 128;
            if (len != 0) {
                b[i] += 0x80;
            }
            i++;
        }
        return Functions.range(b, 0, i);
    }

    public byte[] frontByte() {
        return new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10};
    }

    public byte[] endByte() {
        return new byte[]{(byte) 0x0b};
    }
}
