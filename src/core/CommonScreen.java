/**
 * 
 * @author mike802
 *
 * brand_aware
 * ??? - 2019
 * 
 */
package core;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class CommonScreen extends ConfigScreen{
	
	protected Properties properties;
	protected IOScreen ioScreen;
	protected JLayeredPane layeredPane;
	protected JFrame screen;
	protected Calendar calendar;
	
	//initial screen
	protected JButton logo;
	protected JLabel background;
	
	//menu
	//protected JMenuBar menuBar;
	//protected JMenu file, edit, about;
	
	//opening screen/entry view
	protected JLabel altLogo;
	protected JButton newPost = null;
	protected JComboBox<String> showSelector = null;
	protected String[] dispChoices;
	protected JScrollPane entryViewer;
	protected String[] entryList = null;
	protected ListSelectionModel lsm;
	protected JList<String> titleList;
	protected int entryIndex = -1;
	
	//new entry
	protected JButton save, close, previous, next;
	protected JTextArea entryTextView = null;
	protected JScrollPane entryScroll;
	protected JTextField dateDisplay;
	
	//all entries cache
	ConcurrentHashMap<String, String> entryMap = null;
	
	protected boolean initialized = false;

}
