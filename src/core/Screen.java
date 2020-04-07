/**
 * 
 * @author mike802
 *
 * brand_aware
 * ??? - 2019
 * 
 */
package core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Screen extends CommonScreen{
	
	ButtonHandler handler;
	ListSelectionHandler listHandler;
	
	int selection = 0;
	boolean createPost = false;
		
	public Screen(Properties p) throws IOException {
		properties = p;
		calendar = Calendar.getInstance();
		ioScreen = new IOScreen(properties);
		handler = new ButtonHandler();
		listHandler = new ListSelectionHandler();
		entryMap = ioScreen.loadAllEntries();
	}
	
	private void createPage() {
		screen = new JFrame("journal");
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setResizable(false);
		screen.setPreferredSize(new Dimension(BACKGROUND_WIDTH, BACKGROUND_HEIGHT));
		Image company = Toolkit.getDefaultToolkit().getImage(properties.getCompany());
		screen.setIconImage(company);
		screen.setLocation(250, 100);
		
		background = new JLabel();
		ImageIcon icon = new ImageIcon(properties.getBackground());
		background.setIcon(icon);
		background.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		
		logo = new JButton();
		icon = new ImageIcon(properties.getLogo());
		logo.setIcon(icon);
		int currentX = (BACKGROUND_WIDTH / 2) - (LOGO_WIDTH / 2) - 5;
		int currentY = (BACKGROUND_HEIGHT / 2) - (LOGO_HEIGHT / 2);
		logo.setBounds(currentX, currentY, LOGO_WIDTH, LOGO_HEIGHT);
		logo.addActionListener(handler);
		
		layeredPane = new JLayeredPane();
		layeredPane.add(background);
		layeredPane.add(logo);
		
		layeredPane.moveToBack(background);
		layeredPane.moveToFront(logo);
		
		screen.add(layeredPane);
		screen.pack();
		screen.setVisible(true);
		
	}
	
	private class ButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == logo) {
				try {
					layeredPane.moveToBack(logo);
					doLoadEntryList();
					doDisplayEntries();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(event.getSource() == newPost) {
				createPost = true;
				loadEntryNewPost(null, "");
				updateTitle();
			}else if(event.getSource() == showSelector) {
				try {
					int index = showSelector.getSelectedIndex();
					selection = index;
					doLoadEntryList(index);
					doDisplayEntries();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(event.getSource() == save) {
				try {
					String text = entryTextView.getText();
					if(text.contains(properties.getSeperatorString())) {
						JOptionPane.showMessageDialog(null, 
								"Please remove character sequence: " + properties.getSeperatorString() + " from text.", 
								"banned character sequence",
								JOptionPane.OK_OPTION, 
								new ImageIcon(properties.getCompany()));
					}else if(text.compareTo("") == 0) {
						JOptionPane.showMessageDialog(null, 
								"Please create a post", 
								"banned character sequence",
								JOptionPane.OK_OPTION, 
								new ImageIcon(properties.getCompany()));
					}else {
						updateTitle();
						addEntry();
						ioScreen.saveAllTitles(entryMap);
						ioScreen.saveAllEntries(entryMap);
						hideNewPost();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(event.getSource() == close) {
				try {
					if(createPost) {
						hideNewPost();
					}else {
						hideShowPost();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(event.getSource() == previous) {
				entryIndex--;
				createPost = false;
				loadEntry(entryIndex);
				previous.setEnabled(true);
				next.setEnabled(true);
				if(entryIndex == 0) {
					previous.setEnabled(false);
				}else if(entryIndex == entryList.length - 1) {
					next.setEnabled(false);
				}
			}else if(event.getSource() == next) {
				entryIndex++;
				createPost = false;
				loadEntry(entryIndex);
				previous.setEnabled(true);
				next.setEnabled(true);
				if(entryIndex == 0) {
					previous.setEnabled(false);
				}else if(entryIndex == entryList.length - 1) {
					next.setEnabled(false);
				}
			}
		}
	}
	
	private class ListSelectionHandler implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent event) {
			if(entryList != null) {
				entryIndex = lsm.getLeadSelectionIndex();
				if(entryIndex >= 0 && entryIndex <= entryList.length) {
					createPost = false;
					loadEntry(entryIndex);
					previous.setEnabled(true);
					next.setEnabled(true);
					if(entryIndex == 0) {
						previous.setEnabled(false);
						if(entryIndex == entryList.length - 1) {
							next.setEnabled(false);
						}
					}else if(entryIndex == entryList.length - 1) {
						next.setEnabled(false);
					}
				}
			}
		}
	}
	
	private void addEntry() {
		String entry = entryTextView.getText();
		String title = dateDisplay.getText();
		if(entryMap == null) {
			entryMap = new ConcurrentHashMap<String, String>();
		}
		entryMap.put(title, entry);
	}
	
	private void doLoadEntryList() throws IOException {
		doLoadEntryList(0);
	}
	
	private void doLoadEntryList(int selection) throws IOException {
		entryList = loadTitles(selection);
		if(entryList != null) {
			titleList = new JList<String>(entryList);
			titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			titleList.setFont(new Font("courier", Font.BOLD, 20));
			lsm = titleList.getSelectionModel();
			lsm.addListSelectionListener(listHandler);
			entryViewer = new JScrollPane(titleList);
			entryViewer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			layeredPane.add(entryViewer);
		}else {
			titleList = new JList<String>(new String[] {properties.getNullEntryString()});
			titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			titleList.setFont(new Font("courier", Font.BOLD, 20));
			lsm = titleList.getSelectionModel();
			lsm.addListSelectionListener(listHandler);
			entryViewer = new JScrollPane(titleList);
			entryViewer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		int currentX = (BACKGROUND_WIDTH / 2) - (ENTRY_SCROLL_WIDTH / 2) - 5;
		int currentY = BACKGROUND_HEIGHT - ENTRY_SCROLL_HEIGHT - 50 - 5;
		entryViewer.setBounds(currentX, currentY, ENTRY_SCROLL_WIDTH, ENTRY_SCROLL_HEIGHT);
	}
	
	private String[] loadTitles(int selection) throws IOException {
		ArrayList<String> titles = ioScreen.loadAllTitles();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		month++;
		for(int x = 0; x < titles.size(); x++) {
			if(selection == 1) {
				String token = getTokenYear(titles.get(x));
				if(Integer.parseInt(token) != year) {
					titles.remove(x);
					x--;
				}
			}else if(selection == 2) {
				String token = getTokenMonth(titles.get(x));
				if(token.startsWith("" + 0)) {
					token = token.substring(1, token.length());
				}
				if(Integer.parseInt(token) != month) {
					titles.remove(x);
					x--;
				}
			}
		}
		
		String[] list = null;
		if(titles != null) {
			list = new String[titles.size()];
			for(int x = 0; x < titles.size(); x++) {
				list[x] = titles.get(x);
			}
		}
		return list;
	}
	private String getTokenMonth(String line) {
		String[] lineSplit = line.split(" - ");
		return lineSplit[2];
	}
	private String getTokenYear(String line) {
		String[] lineSplit = line.split(" - ");
		return lineSplit[1];
	}
	
	private void doDisplayEntries() throws Exception {		
		int currentX;
		int currentY = BACKGROUND_HEIGHT - ENTRY_SCROLL_HEIGHT - 50 - 5;
		if(newPost == null) {
			int newPostY = currentY - (50 + NEWENTRY_BUTTON_HEIGHT);
			currentX = 65;
			newPost = new JButton(NEWPOST_BUTTON_LABEL);
			newPost.setBounds(currentX, newPostY, NEWENTRY_BUTTON_WIDTH, NEWENTRY_BUTTON_HEIGHT);
			newPost.addActionListener(handler);
			layeredPane.add(newPost);
		}
		
		if(showSelector == null) {
			currentY = currentY - (50 + SHOW_SELECTOR_HEIGHT);
			currentX = BACKGROUND_WIDTH - 75 - SHOW_SELECTOR_WIDTH;
			dispChoices = ioScreen.loadFilters();
			showSelector = new JComboBox<String>(dispChoices);
			showSelector.setBounds(currentX, currentY, SHOW_SELECTOR_WIDTH, SHOW_SELECTOR_HEIGHT);
			showSelector.addActionListener(handler);
			layeredPane.add(showSelector);
		}
		
		if(altLogo == null) {
			currentY = currentY - (15 + ALT_LOGO_HEIGHT);
			currentX = (BACKGROUND_WIDTH / 2) - (ALT_LOGO_WIDTH / 2) - 5;
			altLogo = new JLabel();
			altLogo.setIcon(new ImageIcon(properties.getAltLogo()));
			altLogo.setBounds(currentX, currentY, ALT_LOGO_WIDTH, ALT_LOGO_HEIGHT);
			layeredPane.add(altLogo);
		}
		
		layeredPane.moveToFront(entryViewer);
		layeredPane.moveToFront(newPost);
		showSelector.setSelectedIndex(selection);
		layeredPane.moveToFront(showSelector);
		layeredPane.moveToFront(altLogo);
	}
	
	private void loadEntry(int selection) {
		String key = entryList[selection];
		if(entryMap != null) {
			String entry = entryMap.get(key);
			if(entry.compareTo(properties.getNullEntryString()) != 0) {
				loadEntry(key, entry);
			}
		}
	}
	
	private void loadEntry(String title, String entry) {
		createEntryView(entry);
		entryTextView.setText(entry);
		//entryScroll = new JScrollPane(entryTextView);
		//entryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dateDisplay.setText(title);
		layeredPane.moveToFront(background);
		layeredPane.moveToBack(newPost);
		layeredPane.moveToBack(showSelector);
		layeredPane.moveToBack(entryViewer);
		layeredPane.moveToBack(altLogo);
		
		layeredPane.moveToFront(entryScroll);
		layeredPane.moveToFront(dateDisplay);
		layeredPane.moveToFront(previous);
		layeredPane.moveToFront(next);
		int currentX = (BACKGROUND_WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2);
		int currentY = 100 + CLOSE_BUTTON_HEIGHT + 15 + ENTRY_VIEW_HEIGHT + 30;;
		close.setBounds(currentX, currentY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		layeredPane.moveToFront(close);
	}
	
	private void createEntryView(String entry) {
		int currentX = (BACKGROUND_WIDTH / 2) - (ENTRY_VIEW_WIDTH / 2) - 5;
		int currentY = 100;
		if(entryTextView == null) {
			entryTextView = new JTextArea(entry);
			entryTextView.setFont(new Font("courier", Font.PLAIN, 18));
			entryTextView.setWrapStyleWord(true);
			entryTextView.setLineWrap(true);
		
			//entryTextView.setBounds(currentX, currentY, ENTRY_VIEW_WIDTH, ENTRY_VIEW_HEIGHT);
			entryScroll = new JScrollPane(entryTextView);
			entryScroll.setBounds(currentX, currentY, ENTRY_VIEW_WIDTH, ENTRY_VIEW_HEIGHT);
			entryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			layeredPane.add(entryScroll);
		}
		
		int dateY = currentY - 50;
		if(dateDisplay == null) {
			dateDisplay = new JTextField();
			dateDisplay.setEditable(false);
			dateDisplay.setBounds(currentX, dateY, DATE_DISPLAY_WIDTH, DATE_DISPLAY_HEIGHT);
			layeredPane.add(dateDisplay);
		}
		//updateTitle();
		
		
		int previousX = currentX + 15;
		currentY += ENTRY_VIEW_HEIGHT + 30;
		if(previous == null) {
			previous = new JButton("previous");
			previous.addActionListener(handler);
			previous.setBounds(previousX, currentY, SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT);
			layeredPane.add(previous);
		}
		
		currentX = (BACKGROUND_WIDTH / 2) + (ENTRY_VIEW_WIDTH / 2) - CLOSE_BUTTON_WIDTH - 30;
		if(next == null) {
			next = new JButton("next");
			next.addActionListener(handler);
			next.setBounds(currentX, currentY, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT);
			layeredPane.add(next);
		}
		
		currentX = (BACKGROUND_WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2);
		currentY = currentY + CLOSE_BUTTON_HEIGHT + 15;
		if(close == null) {
			close = new JButton("close");
			close.addActionListener(handler);
			close.setBounds(currentX, currentY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			layeredPane.add(close);
		}
	}
	
	private void loadEntryNewPost(String title, String entry) {
		layeredPane.moveToFront(background);
		layeredPane.moveToBack(newPost);
		layeredPane.moveToBack(showSelector);
		layeredPane.moveToBack(entryViewer);
		layeredPane.moveToBack(altLogo);
		
		createEntryViewNewPost(entry);
		entryTextView.setText(entry);
		
		layeredPane.moveToFront(entryScroll);
		layeredPane.moveToFront(dateDisplay);
		layeredPane.moveToFront(save);
		int currentY = 100 + ENTRY_VIEW_HEIGHT + 30;
		int currentX = (BACKGROUND_WIDTH / 2) + (ENTRY_VIEW_WIDTH / 2) - CLOSE_BUTTON_WIDTH - 30;
		close.setBounds(currentX, currentY, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT);
		layeredPane.moveToFront(close);
	}
	
	private void createEntryViewNewPost(String entry) {
		int currentX = (BACKGROUND_WIDTH / 2) - (ENTRY_VIEW_WIDTH / 2) - 5;
		int currentY = 100;
		if(entryTextView == null) {
			entryTextView = new JTextArea(entry);
			entryTextView.setFont(new Font("courier", Font.PLAIN, 18));
			entryTextView.setWrapStyleWord(true);
			entryTextView.setLineWrap(true);
			//entryTextView.setBounds(currentX, currentY, ENTRY_VIEW_WIDTH, ENTRY_VIEW_HEIGHT);
			entryScroll = new JScrollPane(entryTextView);
			entryScroll.setBounds(currentX, currentY, ENTRY_VIEW_WIDTH, ENTRY_VIEW_HEIGHT);
			entryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			layeredPane.add(entryScroll);
		}
		
		int dateY = currentY - 50;
		if(dateDisplay == null) {
			dateDisplay = new JTextField();
			dateDisplay.setEditable(false);
			dateDisplay.setBounds(currentX, dateY, DATE_DISPLAY_WIDTH, DATE_DISPLAY_HEIGHT);
			layeredPane.add(dateDisplay);
		}else {
		}
		updateTitle();
		
		
		int saveX = currentX + 15;
		currentY += ENTRY_VIEW_HEIGHT + 30;
		if(save == null) {
			save = new JButton("save");
			save.addActionListener(handler);
			save.setBounds(saveX, currentY, SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT);
			layeredPane.add(save);
		}
		
		currentX = (BACKGROUND_WIDTH / 2) + (ENTRY_VIEW_WIDTH / 2) - CLOSE_BUTTON_WIDTH - 30;
		if(close == null) {
			close = new JButton("close");
			close.addActionListener(handler);
			close.setBounds(currentX, currentY, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT);
			layeredPane.add(close);
		}
	}
	
	private void updateTitle() {
		String dateText = "";
		calendar = Calendar.getInstance();
		dateText += " - " + calendar.get(Calendar.YEAR);
		if(calendar.get(Calendar.MONTH) < 9) {
			dateText += " - 0" + (calendar.get(Calendar.MONTH) + 1);
		}else {
			dateText += " - " +(calendar.get(Calendar.MONTH) + 1);
		}
		if(calendar.get(Calendar.DATE) < 10) {
			dateText += " - 0" + calendar.get(Calendar.DATE);
		}else {
			dateText += " - " + calendar.get(Calendar.DATE);
		}
		if(calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			dateText += " - 0" + calendar.get(Calendar.HOUR_OF_DAY);
		}else {
			dateText += " - " + calendar.get(Calendar.HOUR_OF_DAY);
		}
		if(calendar.get(Calendar.MINUTE) < 10) {
			dateText += ":0" + calendar.get(Calendar.MINUTE);
		}else {
			dateText += ":" + calendar.get(Calendar.MINUTE);
		}
		if(calendar.get(Calendar.SECOND) < 10) {
			dateText += ":0" + calendar.get(Calendar.SECOND);
		}else {
			dateText += ":" + calendar.get(Calendar.SECOND);
		}
		dateDisplay.setText(dateText);
	}
	
	private void hideNewPost() throws Exception {
		layeredPane.moveToFront(background);
		layeredPane.moveToBack(dateDisplay);
		layeredPane.moveToBack(entryScroll);
		layeredPane.moveToBack(save);
		layeredPane.moveToBack(close);
		doLoadEntryList();
		doDisplayEntries();
	}
	
	private void hideShowPost() throws Exception {
		layeredPane.moveToFront(background);
		layeredPane.moveToBack(dateDisplay);
		layeredPane.moveToBack(entryScroll);
		layeredPane.moveToBack(next);
		layeredPane.moveToBack(previous);
		layeredPane.moveToBack(close);
		doLoadEntryList();
		doDisplayEntries();
	}
	
	public void init() {
		if(!initialized) {
			createPage();
			initialized = true;
		}
	}
}
