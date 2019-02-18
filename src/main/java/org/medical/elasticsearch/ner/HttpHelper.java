package org.medical.elasticsearch.ner;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;
import java.util.List;
import com.alibaba.fastjson.*;

public class HttpHelper {

    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder(
                "http://114.251.235.51:8080/ner");
        Map<String, String> params = new HashMap<String, String>();
        params.put("data", "执行膀胱颈Ｖ形切除术，术后腹胀，腹痛");
        String result = GetPostUrl(sb.toString(), params, "GET",null, 0, 0);
        ArrayList<TermItem> terms = GetNerTerm(result);
    }

    /**
     * get/post url
     * @param sendUrl
     * @param params
     * @param sendType
     * @param charset
     * @param repeat_request_count
     * @param repeat_request_max_count
     * @return
     */
    public static String GetPostUrl(String sendUrl, Map<String, String> params, String sendType, String charset,
                                    int repeat_request_count, int repeat_request_max_count) {
        URL url = null;
        HttpURLConnection httpurlconnection = null;

        try {
            // 构建请求参数
            StringBuffer paramSb = new StringBuffer();
            if (params != null) {
                for (java.util.Map.Entry<String, String> e : params.entrySet()) {
                    paramSb.append(e.getKey());
                    paramSb.append("=");
                    // 将参数值urlEncode编码,防止传递中乱码
                    paramSb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
                    paramSb.append("&");
                }
                paramSb.substring(0, paramSb.length() - 1);
            }
            url = new URL(sendUrl + "?" + paramSb.toString());
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);

            // 设置http请求超时时间30000毫秒（30秒）
            httpurlconnection.setConnectTimeout(30000);
            httpurlconnection.setReadTimeout(30000);
            httpurlconnection.setUseCaches(true);
            /*
             * if (submitMethod.equalsIgnoreCase("POST")) {
             * httpurlconnection.getOutputStream().write(postData.getBytes("GBK"
             * )); httpurlconnection.getOutputStream().flush();
             * httpurlconnection.getOutputStream().close(); }
             */

            int code = httpurlconnection.getResponseCode();
            if (code == 200) {
                DataInputStream in = new DataInputStream(httpurlconnection.getInputStream());
                int len = in.available();
                byte[] by = new byte[len];
                in.readFully(by);
                String rev = new String(by, "UTF-8");

                in.close();

                return rev;
            } else {
                // http 请求返回非 200状态时处理
                return "<?xml version=\"1.0\" encoding=\"utf-8\" ?><error>发送第三方请求失败</error>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        return null;
    }

    public static ArrayList<TermItem> GetNerTerm(String result) {
        ArrayList<TermItem> terms = new ArrayList<TermItem>();
        if (null != result || "[]" != result) {
            JSONArray list = JSONObject.parseArray(result);
            int size = list.size();
            if(size > 0) {
                for (int i = 0; i < size; i++) {
                    JSONObject termObj = list.getJSONObject(i);
                    String nerContent = termObj.get("nerContent").toString();
                    int start = Integer.parseInt(termObj.get("start").toString());
                    int end = Integer.parseInt(termObj.get("end").toString());
                    String nerType = termObj.get("nerType").toString();
                    TermItem term = new TermItem(nerContent, start, end, nerType);

                    terms.add(term);
                }
            }
        }

        return terms;
    }
}
