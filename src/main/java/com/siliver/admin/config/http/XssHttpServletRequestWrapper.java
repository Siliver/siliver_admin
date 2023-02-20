package com.siliver.admin.config.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.siliver.admin.util.SecurityStringUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 所有参数的MAP集合
     */
    private final Map<String, String[]> parameterMap;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.parameterMap = request.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<>(parameterMap.keySet());
        return vector.elements();
    }

    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if (Objects.isNull(results) || 0 == results.length) {
            return null;
        } else {
            String value = results[0];
            if (StringUtils.hasText(value)) {
                value = SecurityStringUtils.cleanXSS(value);
            }
            return value;
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = parameterMap.get(name);
        if (Objects.isNull(values)) {
            return null;
        }
        int length = values.length;
        String[] needFilterValues = new String[length];
        for (int i = 0; i < length; i++) {
            needFilterValues[i] = SecurityStringUtils.cleanXSS(values[i]);
            if (Objects.equals(needFilterValues[i], values[i])) {
                log.info("此次请求包含xss攻击");
                log.info("此次xss攻击" + values[i] + "过滤后" + needFilterValues[i]);
            }
        }
        return needFilterValues;
    }

    @Override
    public ServletInputStream getInputStream() {
        if (super.getContentType().equals("application/json")) {
            String string;
            try (ServletInputStream servletInputStream = super.getInputStream()) {
                string = getRequestBody(servletInputStream);
            } catch (IOException e) {
                log.error("读取输入流异常", e);
                return null;
            }
            Object parameterObj = JSON.parse(string);
            if (parameterObj instanceof JSONObject) {
                Map<String, Object> map = JSON.parseObject(string, new TypeReference<Map<String, Object>>() {
                });
                Map<String, Object> resultMap = new HashMap<>(map.size());
                for (String key : map.keySet()) {
                    Object val = map.get(key);
                    if (map.get(key) instanceof String) {
                        resultMap.put(key, SecurityStringUtils.stripXSS(val.toString()));
                    } else {
                        resultMap.put(key, val);
                    }
                }
                string = JSON.toJSONString(resultMap);
            } else {
                string = SecurityStringUtils.stripXSS(string);
            }
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() {
                    return byteArrayInputStream.read();
                }
            };
        } else {
            try {
                return super.getInputStream();
            } catch (IOException e) {
                log.error("读取输入流异常", e);
            }
        }
        return null;
    }

    private String getRequestBody(InputStream stream) {
        String line;
        StringBuilder body = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        try {
            while (StringUtils.hasText(line = reader.readLine())) {
                body.append(line);
            }
        } catch (IOException e) {
            log.error("读取requestBody异常", e);
        }
        return body.toString();
    }
}
