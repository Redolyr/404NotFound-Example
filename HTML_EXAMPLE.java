
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.zip.Deflater;

/**
 * <pre>
 * you can trying display 404 not found page.
 * Q. how to use this application?
 * A. 1. Staring this program (or batch file).
 *    2. You do browse 'localhost:8888' or 'localhost'. (because you can use instead address is 127.0.0.1)
 *    3. End of you do browse.
 * 
 * 404 Not Foundページを体験できます。
 * 起動し方は直接起動するか、バッチファイルで起動する。
 * 次にlocalhost:8888かlocalhostをURLに打ち込んで飛ぶ。代替にlocalhostを127.0.0.1にすることもできる。
 * それで終わり
 * </pre>
 * 
 * @author redolyr
 *
 */
public class HTML_EXAMPLE {
	
	public static final String NOT_FOUND_404_TEMPLATE_PAGE = "<html><head><title>404 Not Found</title><meta name=\"robots\" content=\"noindex,nofollow,nositelinkssearchbox,nocache,nosnippet,noimageindex,noodp,nodiff,notranslate\"></head><body style=\"font-family: 'Times New Roman', serif; font-size: 48px;\"><p><b>Not Found</b></p><p style=\"font-size: 60.76%;\">The requested URL %s was not found on this server.</p><hr><p><br></p></body></html>";
	
	public static final String NOT_FOUND_404_TEMPLATE_PAGE_2 = "<i style=\"font-size: 60.76%;\">{3} Server at {1} Port {2}</i>";

	public static final String NOT_FOUND_404_STATUS_CODE =
			"HTTP/1.1 200 OK\r\n" +
			"Server: Unknown\r\n" +
			"Content-Length: %d\r\n" +
			"Connection: Keep-Alive\r\n" +
			"Content-Type: text/html\r\n" +
			"Accept-Encoding: gzip, deflate, br\r\n" +
			"\r\n";
	
	public static final String get404NotFoundTemplate(String url, String afterCode) {
		String in = NOT_FOUND_404_TEMPLATE_PAGE.replace("%s", url == null ? "/" : url);
		return String.format(NOT_FOUND_404_STATUS_CODE, in.length()) + in;
	}
	
	public static String createStatus(String connection, String accept, String encoding, String language, String agent) {
		return String.format("Connection: %s\r\nUser-Agent: %s\r\nAccept: %s\r\nAccept-Encoding: %s\r\nAccept-Language: %s\r\n", connection, agent,accept, encoding, language);
	}
	
	public static byte[] deflate(byte[] bytes) {
		Deflater def = new Deflater();
        def.setLevel(Deflater.BEST_SPEED);
        def.setInput(bytes);
        def.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while(!def.finished()) {
        	len = def.deflate(buff);
            byteArrayOutputStream.write(buff, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
	}

	public static void main(String[] args) throws IOException {
		
		System.gc();
		
		ServerSocket server = new ServerSocket(8888);
		Socket sock = null;
		DataOutputStream dos = null;
		BufferedReader bufferedReader = null;
		String line = "";
		StringTokenizer token = null;
		String requestURL = "";
		String requestedHost = "";
		int requestedPort = 0;
		String connection = "";
		String accept = "";
		String encoding = "";
		String language = "";
		String agent = "";
		
		String notFound404_serverName = "Not Found/Unknown";//<here> Server at <ip> Port <port>

		System.out.println("Server " + server.getInetAddress() + "(" + server.getLocalSocketAddress() + ") ReceiveBufferSize: " + server.getReceiveBufferSize() + " soTimeout:" + server.getSoTimeout());
		while (server.isBound()) {
			sock = server.accept();
			if (!sock.isConnected()) {
				if (!sock.isClosed()) sock.close();
				continue;
			}
			System.out.println("Access " + sock.getInetAddress() + "(" + sock.getLocalAddress() + ") sendBufferSize: " + sock.getSendBufferSize() + " receiveBufferSize: " + sock.getReceiveBufferSize() + " trafficClass: " + sock.getTrafficClass() + " soTimeout: " + sock.getSoTimeout() + " soLinger: " + sock.getSoLinger() + " TCPNoDelay: " + sock.getTcpNoDelay() + " keep-alive: " + sock.getKeepAlive() + " OOBInline: " + sock.getOOBInline() + " closed: " + sock.isClosed() + " connected: " + sock.isConnected() + " bound: " + sock.isBound() + " inputStreamShutdown: " + sock.isInputShutdown() + " outputStreamShutdown: " + sock.isOutputShutdown());
			System.out.println("Access " + sock.getInetAddress() + "" + sock.getInetAddress().getHostAddress() + " " + sock.getInetAddress().getHostName() + " canonical: " + sock.getInetAddress().getCanonicalHostName() + " loopbackAddress: " + sock.getInetAddress().isLoopbackAddress() + " address: " + Arrays.toString(sock.getInetAddress().getAddress()) + " anyLocalAddress: " + sock.getInetAddress().isAnyLocalAddress() + " linkLocalAddress: " + sock.getInetAddress().isLinkLocalAddress() + " MCGlobal: " + sock.getInetAddress().isMCGlobal() + " MCLinkLocal: " + sock.getInetAddress().isMCLinkLocal() + " MCNodeLocal: " + sock.getInetAddress().isMCNodeLocal() + " MCOrgLocal: " + sock.getInetAddress().isMCOrgLocal() + " MCSiteLocal: " + sock.getInetAddress().isMCSiteLocal() + " MulticastAddress: " + sock.getInetAddress().isMulticastAddress() + " siteLocalAddress: " + sock.getInetAddress().isSiteLocalAddress());
			bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
			dos = new DataOutputStream(sock.getOutputStream());
			System.out.println(" --- GET RESPONSES BEGIN --- ");
			while (bufferedReader.ready()) {
				line = bufferedReader.readLine();
				System.out.printf("%04d: %s\n", line.length(), line);
				if (line.length() == 0) continue;
				token = new StringTokenizer(line);
				String tokens = token.nextToken().toLowerCase();
				if (tokens.startsWith("get")) {
					requestURL = token.nextToken();
				} else if (tokens.startsWith("host")) {
					String input = token.nextToken();
					requestedHost = input;
					requestedPort = Integer.parseInt(input.substring(input.indexOf(":") + 1));
				} else if (tokens.startsWith("connection"))
					connection = token.nextToken();
				else if (tokens.startsWith("accept"))
					accept = token.nextToken();
				else if (tokens.startsWith("accept-encoding"))
					encoding = token.nextToken();
				else if (tokens.startsWith("accept-language"))
					language = token.nextToken();
				else if (tokens.startsWith("user-agent"))
					agent = token.nextToken();
			}
			System.out.println(" --- GET RESPONSES END --- ");
			if (requestURL.isEmpty()) continue;
			dos.writeBytes(get404NotFoundTemplate(requestURL, NOT_FOUND_404_TEMPLATE_PAGE_2.replace("{1}", requestedHost.replace(":" + requestedPort, "")).replace("{2}", "" + requestedPort).replace("{3}", "" + notFound404_serverName).replace("Connection: Keep-Alive\r\n", createStatus(connection, accept, encoding, language, agent))));
			sock.close();
			System.gc();
			System.out.println();
		}
		server.close();
	}
}
