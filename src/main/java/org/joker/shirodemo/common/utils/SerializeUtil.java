package org.joker.shirodemo.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * Java原生版的 Serialize
 * 
 */

@SuppressWarnings("unchecked")
public class SerializeUtil {
	static final Class<?> CLAZZ = SerializeUtil.class;


    /**
     * 将对象序列化转为byte数组
     * @param value
     * @return
     */
    public static byte[] serialize(Object value) {
        if (value == null) { 
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (Exception e) {
        	LoggerUtils.fmtError(CLAZZ,e, "serialize error %s", JsonUtil.toJson(value));
        } finally {
            close(os);
            close(bos);
        }
        return rv;
    }

    /**
     * 反序列化
     * @param in
     * @return
     */
	public static Object deserialize(byte[] in) {
        return deserialize(in, Object.class);
    }

    public static <T> T deserialize(byte[] in, Class<T>...requiredType) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
            }
        } catch (Exception e) {
        	 LoggerUtils.fmtError(CLAZZ,e, "serialize error %s", in);
        } finally {
            close(is);
            close(bis);
        }
        return (T) rv;
    }

    private static void close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
            	 LoggerUtils.fmtError(CLAZZ, "close stream error");
            }
    }

}
