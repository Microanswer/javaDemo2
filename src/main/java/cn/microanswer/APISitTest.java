package cn.microanswer;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class APISitTest {
    static String defaultCharset = "UTF-8";
    static String algorithm = "SHA256WITHRSA";
    static String privateKey =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDHz+WsFaIjIJ2Ao/PwxPFbW" +
            "zeHKrxUR+FTGmm69JyOna1BKapzCj89Gh4nCkJFSTe/yepJoLR6PJeNHVKh5JhpuxfUgwyxAXr" +
                    "+vdJwBdpDDE4sk7rdE+YJQ/NbKGILQiQhFy/wUH0PbcISBw+9Pw5pQX6rmprFBuUO6" +
                    "jRxRpPUICaG3/3gXrjfBLhw5DeSfo4jLFyqa0vDTZWatCnXN9kfAYdjTHQnGeRKV0G" +
                    "dSYYgT0qXwYRQ7iXWpS8DGdcTgz1PeRmE1m1rjURR13yyUPOFSxB3GGJmLtbSJolEN" +
                    "+iaAQe1eZcKZPB79rrNZbg8wA2NJFMIWh4XA+xfTrocCLU9AgMBAAECggEBALw4a6q" +
                    "R6lzasWNnjjhE3PLo10Ldj/B4kZn97JEvMXbqszUJ8cVSZsXBH5w4/rpTwU9J/ggDe" +
                    "V8YSs6kS52zP5uBoW4PUNpAgcMLvvrFmJfGNLGrK8BrqSsbNletj8X5NR1ywTbIKzZ" +
                    "odwMSYdIsXv/psVAnYBmvaACHwITgLgsH3MfCLHF0nrX3dDgAUMSY6DYq1gRB8KYqL" +
                    "adM/YgKgtlIPbTq6clg4jLhoLUZMXLAXQWDLdru1DdiBaI6cdE7LK4MUTBbaw/l6Dn" +
                    "8GuiyrOj4Jv+Lj/niptNUI3oCQ/cR9DQ2LD2bWtoFmq7EKmKOqD6mI0INNCTQSx2Ve" +
                    "KhT8cUCgYEA/+gZTGADncDxOsB+/wekDX2fdi23EgW8vGZodm8vdBn2FWe4D6vIZgc" +
                    "tUADUfpZt8KcYqra7uYi2fPlfsVa8NPa1kCot/GmebIq1UEU1/NoxsAOA4UxRm5CeX" +
                    "bZZK3V1p//2VX2Rk7eyAfPTQrTbQZj/ApCl2wLDJQJ2iSUTNccCgYEAx+KPKL8+zbp" +
                    "w+6QHDZpkjIB/ua9oFUxhfTXtRpU6ZYsFmj4wyeTBT++Owu2Y3hq8TQ5+oV8xry/Dh" +
                    "gTmZbLFeUp/vYA93Sl0sg0WgDhzmAMG1SWLG4QeOYsf4ljDfZiyBk71LZJqyZ4SHtR" +
                    "cOimyIvfMJrJP/y4YBDViu7/IrNsCgYEAtYNWdmLgDcQuFsM1EQXFakBBnBx+7syc+" +
                    "3EUBXWFzi8f6jzgRFVPVncev9Fm58chggGzsFreupHrBqgHJQ1lf5fCXthcN95enZS" +
                    "hWKdOzOOjPLqvbp6of9RLUCMoCnhNFPWsLyVa3dD89P+SSI1pu5SexJ3+1mKWMikWF" +
                    "N70Sj8CgYAd0P3p5h1IMveHkgA7VzyIeOKUoEW2Z55jPxgV30zGuOYtC/nbI/RyKBn" +
                    "zmd2jw/kyxWa+S6TtrQJNi42SvggW/7XuXqLbysNNjAY/OiyEEG+EfAU0JZEcpFQtP" +
                    "3Bp280yg7SvB5hGWv34+1SGmJGqBay+WPr15bpPZmdD+mfE4QKBgFcq67xhDA7jKB6" +
                    "h7VPR0BsBVGA4H56gO4AwABq39iJfokcQtx/zI6d9esbYyN/vT8UCaCSdeOQJ59UJH" +
                    "U015jryeO7xg6gzBLS1AiQqzWsuTRhu8fOBnPHfSjkbTvA+fFUgSHD+bu5L0xTL8OO" +
                    "wFubBsmllkbl0R2HmDjwN7Vbs";    //私钥
    static String publicKey = "";    //公钥

    static String path = "https://car.etaiping.com:6004/ecms2/portal";    //地址
    static String clientName = "xxxxxx";
    static String api = "carType";

    public static void main(String[] args) throws Exception {
        new APISitTest().SitTest();
    }

    public void SitTest() throws Exception {
        com.sun.org.apache.xml.internal.security.Init.init();
        //测试报文，请根据自己的详细情况使用
        String text = "Javascript"; //getXMLAsResource("test.xml", APISitTest.class, defaultCharset);

        //对报文进行加签
        byte[] signBytes;
        String respEncode = Base64.encode(text.getBytes(defaultCharset));
        signBytes = sign(respEncode.replace("\n", "").getBytes(), Base64.decode(privateKey), algorithm);
        String respSign = Base64.encode(signBytes, Integer.MAX_VALUE);

        System.out.println("内容加签结果：" + respSign);

        /*

        //约过SSL验证
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);

        //构建地址并打开请求
        URL url = new URL(String.format("%s/%s/%s", path, clientName, api));
        InetSocketAddress addr = new InetSocketAddress("xxx.xxx.xxx.xxx", 11111);//可配置代理 
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
//	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(proxy);

        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

        //加签且发送请求并获取结果
        conn.setRequestProperty("TP-SIGN", respSign);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        String rsp;
        OutputStream out = null;

        try {
            out = conn.getOutputStream();
            out.write(text.getBytes());
            rsp = getResponseAsString(conn);
        } catch (IOException e) {
            throw e;
        }

        System.out.println(rsp);

        //关闭流以及连接
        conn.disconnect();
        if (out != null) {
            out.close();
        }
        if (conn != null) {
            conn.disconnect();

        }*/

    }

    public static byte[] sign(final byte[] text, final byte[] privateKeyData,
                              final String algorithm) throws Exception {
        System.out.println(Arrays.toString(privateKeyData));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Signature signatureChecker = Signature.getInstance(algorithm);
        signatureChecker.initSign(privateKey);
        signatureChecker.update(text);
        return signatureChecker.sign();
    }

    public static boolean verify(final byte[] text, final byte[] signedText,
                                 final byte[] publicKeyData, final String algorithm)
            throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Signature signatureChecker = Signature.getInstance(algorithm);
        signatureChecker.initVerify(publicKey);
        signatureChecker.update(text);
        return signatureChecker.verify(signedText);
    }


    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static String getXMLAsResource(String fileName, Class rClass, String charset) throws IOException {
        InputStream is = rClass.getResourceAsStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        return getStreamAsString(conn.getInputStream(), defaultCharset);
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}