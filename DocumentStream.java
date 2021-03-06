/**
 * @author Deepak Rohan Sekar, Ganesh Ramamoorthy
 * @version 1.0
 * @date March 21, 2014 
 * 
 * class DocumentStream extends the class OutputStream This has been
 * used to generate the output of the instructions and the output 
 * of the credits with the specified attributes, Here this class has been 
 * used to generate a plain text or styled document.
 */
import java.io.OutputStream;
import java.io.IOException;
import javax.swing.text.*;
public class DocumentStream extends OutputStream {

	/**
	 * Output stream is generated with the attributes of 
	 * @param doc document to write to.
	 * @param a character attributes to use for the written text.
	 */
	public DocumentStream(Document doc, AttributeSet a) {
		this.doc = doc;
		this.a = a;
	}

	/**
	 * Output stream to output the document
	 * @param doc the document to write to.
	 */
	public DocumentStream(Document doc) {
		this(doc, null);
	}

	/**
	 * write is implemented from the OutputStream
	 * @param b denotes the byte
	 */
	public void write(int b) throws IOException {
		one[0] = (byte) b;
		write(one, 0, 1);
	}

	/**
	 * write is implemented from the OutputStream
	 * @param b denotes the byte
	 * @param off denotes the offset value
	 * @param len denotes the length
	 */
	public void write(byte b[], int off, int len) throws IOException {
		try {
			doc.insertString(doc.getLength(), new String(b, off, len), a);
		} catch (BadLocationException ble) {
			throw new IOException(ble.getMessage());
		}
	}

	private byte[] one = new byte[1];
	private Document doc;
	private AttributeSet a;
}
