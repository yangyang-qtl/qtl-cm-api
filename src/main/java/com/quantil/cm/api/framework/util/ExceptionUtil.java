package com.quantil.cm.api.framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    private ExceptionUtil(){
    }
    /**
     * 打印堆栈信息
     * @param e
     * @return
     */
    public static String printTrace(Throwable e){
        String stackTrace = null;
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (final Exception ex) {
        }
        return stackTrace;
    }
        
}
