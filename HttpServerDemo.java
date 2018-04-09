import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
//import com.sun.org.apache.xerces.internal.util.URI;

public class HttpServerDemo {
  public static void main(String[] args) throws IOException {
    InetSocketAddress addr = new InetSocketAddress(8091);
    HttpServer server = HttpServer.create(addr, 0);

    server.createContext("/", new MyHandler());
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    System.out.println("Server is listening on port 8091" );
  }
}

class MyHandler implements HttpHandler {
  public void handle(HttpExchange exchange) throws IOException {
    String requestMethod = exchange.getRequestMethod();
    
    String ipAddress = exchange.getRemoteAddress().toString();
    String HostName = exchange.getRemoteAddress().getHostName();
    String Address = exchange.getRemoteAddress().getAddress().toString();
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    String date = df.format(new Date());
    
    if (requestMethod.equalsIgnoreCase("GET")) 
    {
      
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, 0);
         
      ///*
      String response = " this is server 222222"+",your ip:"+ipAddress +"\n";
      String response2 = " ipAddress:"+ipAddress +"\n HostName:"+HostName+"\n Address:"+Address
    		  +"\n now time:"+date;
      
      OutputStream responseBody = exchange.getResponseBody();
      responseBody.write(response.getBytes());
      responseBody.write(response2.getBytes());      
      responseBody.flush(); 
      responseBody.close();
      System.out.println("detect somebody try get..." );
      //*/
      
      java.net.URI requestedUri = exchange.getRequestURI(); 

      String query = ((java.net.URI) requestedUri).getRawQuery();
      System.out.println("detect parameters:" +query);
      
      Map<String, String> params = queryToMap(query);
      for (String key : params.keySet()) {  
    	   System.out.println("key= "+ key + " and value= " + params.get(key));  
    	  }  	 
      
    }
  }
  
  public Map<String, String> queryToMap(String query)
  {
	    Map<String, String> result = new HashMap<String, String>();
	    for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        if (pair.length>1) {
	            result.put(pair[0], pair[1]);
	        }else{
	            result.put(pair[0], "");
	        }
	    }
	    return result;
	}
  
}
