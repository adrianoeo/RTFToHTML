/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtftohtml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

/**
 *
 * @author adriano
 */
public class Converter {

	private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
//	private static final String CONN_URL = "jdbc:oracle:thin:@172.16.0.19:1521:CERESHMLG";
	private static final String CONN_URL = "jdbc:oracle:thin:@172.16.0.14:1521:CERESPRD";
	private static final String USER_NAME = "system";
	private static final String PASSWD = "system1";
	private static Connection conn;

	public static String rtfToHtml(Reader rtf) throws IOException {
		JEditorPane p = new JEditorPane();
		p.setContentType("text/rtf");

		EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
		try {
			kitRtf.read(rtf, p.getDocument(), 0);
			Writer writer = new StringWriter();

			/**
			 * Ao invés de converter para HTML*(devido a formatação), é convertido em TXT e 
			 * inserida a tag <br> no final de cada linha e feito os escapes dos caracteres
			 */
			EditorKit kitTxt = p.getEditorKitForContentType("text/txt"); //para txt
			kitTxt.write(writer, p.getDocument(), 0, p.getDocument().getLength());

//			EditorKit kitHtml = p.getEditorKitForContentType("text/html"); *
//			kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength()); *
			
//			String escaped = StringEscapeUtils.escapeHtml4(writer.toString());
			String escaped = escapeHTML(textToHTML(writer.toString()));
			return escaped;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnection() {
		try {
			Class.forName(DRIVER_NAME);

			conn = DriverManager.getConnection(CONN_URL, USER_NAME, PASSWD);
		} catch (ClassNotFoundException | SQLException ex) {
			System.out.println("Erro na conexão: " + ex.getMessage());
		}

		System.out.println("Conexão Efetuada!");

		return conn;
	}

	public static void getInstrucoes() {
		try {
//			String sql = "select t1.instrucao, t1.id from pcp.inst_proc t1 where t1.id in (46)";
			String sql = "select t1.instrucao, t1.id from pcp.inst_proc t1 where t1.id not in (1041, 1001, 1081, 1101, 1061, 1102, 1103)";

			conn = getConnection();

			PreparedStatement pst = conn.prepareStatement(sql);

//			pst.setInt(1, 1);
//			pst.setInt(2, 121);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String instrucao = rtfToHtml(new StringReader(rs.getString("instrucao")));

				update(instrucao, rs.getInt("id"));
//				PrintWriter writer = new PrintWriter(rs.getString("id") + ".html", "UTF-8");
//				writer.println(instrucao);
//				writer.close();
			}
            
            System.out.println("Finalizado.");
            
			conn.close();
		} catch (IOException | SQLException ex) {
			System.out.println("Erro ao selecionar: " + ex.getMessage());
		}
	}

	private static void update(String instrucao, int id) {
		try {
			String sql = "update pcp.inst_proc t1 set t1.instrucao_html = ?  where t1.id = ?";

			PreparedStatement pst = conn.prepareStatement(sql);

			pst.setString(1, instrucao);
			pst.setInt(2, id);

			pst.executeUpdate();

		} catch (SQLException ex) {
			System.out.println("Erro ao atualizar: " + ex.getMessage());
		}
	}

	public static final String escapeHTML(String s) {
		StringBuilder sb = new StringBuilder();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
				/*				case '<':
				 sb.append("&lt;");
				 break;
				 case '>':
				 sb.append("&gt;");
				 break;
				 case '&':
				 sb.append("&amp;");
				 break;
				 case '"':
				 sb.append("&quot;");
				 break;
				 */
				case 'á':
					sb.append("&aacute;");
					break;
				case 'Á':
					sb.append("&Aacute;");
					break;
				case 'ã':
					sb.append("&atilde;");
					break;
				case 'Ã':
					sb.append("&Atilde;");
					break;
				case 'à':
					sb.append("&agrave;");
					break;
				case 'À':
					sb.append("&Agrave;");
					break;
				case 'â':
					sb.append("&acirc;");
					break;
				case 'Â':
					sb.append("&Acirc;");
					break;
				case 'ä':
					sb.append("&auml;");
					break;
				case 'Ä':
					sb.append("&Auml;");
					break;
				case 'å':
					sb.append("&aring;");
					break;
				case 'Å':
					sb.append("&Aring;");
					break;
				case 'æ':
					sb.append("&aelig;");
					break;
				case 'Æ':
					sb.append("&AElig;");
					break;
				case 'ç':
					sb.append("&ccedil;");
					break;
				case 'Ç':
					sb.append("&Ccedil;");
					break;
				case 'é':
					sb.append("&eacute;");
					break;
				case 'É':
					sb.append("&Eacute;");
					break;
				case 'è':
					sb.append("&egrave;");
					break;
				case 'È':
					sb.append("&Egrave;");
					break;
				case 'ê':
					sb.append("&ecirc;");
					break;
				case 'Ê':
					sb.append("&Ecirc;");
					break;
				case 'ë':
					sb.append("&euml;");
					break;
				case 'Ë':
					sb.append("&Euml;");
					break;
				case 'ï':
					sb.append("&iuml;");
					break;
				case 'Ï':
					sb.append("&Iuml;");
					break;
				case 'í':
					sb.append("&iacute;");
					break;
				case 'Í':
					sb.append("&Iacute;");
					break;
				case 'ô':
					sb.append("&ocirc;");
					break;
				case 'Ô':
					sb.append("&Ocirc;");
					break;
				case 'ö':
					sb.append("&ouml;");
					break;
				case 'Ö':
					sb.append("&Ouml;");
					break;
				case 'ó':
					sb.append("&oacute;");
					break;
				case 'Ó':
					sb.append("&Oacute;");
					break;
				case 'õ':
					sb.append("&otilde;");
					break;
				case 'Õ':
					sb.append("&Otilde;");
					break;
				/*				case 'ø':
				 sb.append("&oslash;");
				 break;
				 case 'Ø':
				 sb.append("&Oslash;");
				 break;
				 case 'ß':
				 sb.append("&szlig;");
				 break;*/
				case 'ù':
					sb.append("&ugrave;");
					break;
				case 'Ù':
					sb.append("&Ugrave;");
					break;
				case 'û':
					sb.append("&ucirc;");
					break;
				case 'Û':
					sb.append("&Ucirc;");
					break;
				case 'ü':
					sb.append("&uuml;");
					break;
				case 'Ü':
					sb.append("&Uuml;");
					break;
				case 'ú':
					sb.append("&uacute;");
					break;
				case 'Ú':
					sb.append("&Uacute;");
					break;
				case 'º':
					sb.append("&ordm;");
					break;
				case 'ª':
					sb.append("&ordf;");
					break;
				/*				case '®':
				 sb.append("&reg;");
				 break;
				 case '©':
				 sb.append("&copy;");
				 break;
				 case '€':
				 sb.append("&euro;");
				 break;
				 // be carefull with this one (non-breaking whitee space)
				 case ' ':
				 sb.append("&nbsp;");
				 break;
				 */
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

	public static String textToHTML(String text) {
		if (text == null) {
			return null;
		}
		int length = text.length();
		boolean prevSlashR = false;
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char ch = text.charAt(i);
			switch (ch) {
				case '\r':
					if (prevSlashR) {
						out.append("<br>");
					}
					prevSlashR = true;
					break;
				case '\n':
					prevSlashR = false;
					out.append("<br>");
					break;
				default:
					if (prevSlashR) {
						out.append("<br>");
						prevSlashR = false;
					}
					out.append(ch);
					break;
			}
		}
		return out.toString();
	}
}
