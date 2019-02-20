
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


public class AddressBook extends Application implements AddressBookJavaFx1Finals {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Stage[] stages = new Stage[NUMBER_OF_OBJECTS];
		Scene[] scenes = new Scene[NUMBER_OF_OBJECTS];
		AddressBookPane[] panes = new AddressBookPane[NUMBER_OF_OBJECTS];
		AddressBookPane tempPane;

		try {
			int i = -1;
			do {
				if ((tempPane = AddressBookPane.getInstance()) != null) {
					i++;
					panes[i] = tempPane;
					scenes[i] = new Scene(panes[i]);
					stages[i] = new Stage();
					stages[i].setTitle(TITLE);
					stages[i].setScene(scenes[i]);
					stages[i].setResizable(true);
					stages[i].show();
					stages[i].setAlwaysOnTop(true);

					stages[i].setOnCloseRequest(event -> {
						AddressBookPane.reduceNumberOfObjects();
					});
				} else {
					System.out.println(SINGLETON_MESSAGE);
					break;
				}
			} while (true);

		} catch (Exception e) {
			AddressBookPane.resetNumberOfObjects();
		}

	}
}

class AddressBookPane extends GridPane implements AddressBookJavaFx1Finals {
	protected static int number_of_objects = 0;
	protected static boolean mainOne = true;

	private RandomAccessFile raf;

	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();

	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	private AddButton jbtAdd;
	private RedoButton jbtRedo;
	private UndoButton jbtUndo;

	public FlowPane jpButton;

	public EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent arg0) {
			((Command) arg0.getSource()).Execute();
		}
	};

	protected AddressBookPane() {

		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(0);
		}

		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(25);
		jtfZip.setPrefWidth(60);

		jbtFirst = new FirstButton(this, raf);
		jbtNext = new NextButton(this, raf);
		jbtPrevious = new PreviousButton(this, raf);
		jbtLast = new LastButton(this, raf);

		Label state = new Label("State");
		Label zp = new Label("Zip");
		Label name = new Label("Name");
		Label street = new Label("Street");
		Label city = new Label("City");

		GridPane p1 = new GridPane();
		p1.add(name, 0, 0);
		p1.add(street, 0, 1);
		p1.add(city, 0, 2);
		p1.setAlignment(Pos.CENTER_LEFT);
		p1.setVgap(8);
		p1.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);

		GridPane adP = new GridPane();
		adP.add(jtfCity, 0, 0);
		adP.add(state, 1, 0);
		adP.add(jtfState, 2, 0);
		adP.add(zp, 3, 0);
		adP.add(jtfZip, 4, 0);
		adP.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);

		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(adP, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(adP, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(adP, Priority.ALWAYS);

		GridPane jpAddress = new GridPane();
		jpAddress.add(p1, 0, 0);
		jpAddress.add(p4, 1, 0);
		GridPane.setHgrow(p1, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(p1, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);

		jpAddress.setStyle("-fx-border-color: grey;" + " -fx-border-width: 1;" + " -fx-border-style: solid outside ;");

		jpButton = new FlowPane();

		jpButton.setHgap(5);
		jpButton.getChildren().addAll(jbtFirst, jbtNext, jbtPrevious, jbtLast);
		jpButton.setAlignment(Pos.CENTER);

		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(jpAddress, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(jpAddress, Priority.ALWAYS);

		this.setVgap(5);
		this.add(jpAddress, 0, 0);
		this.add(jpButton, 0, 1);

		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbtFirst.Execute();
	}

	public static AddressBookPane getInstance() {
		if (number_of_objects < NUMBER_OF_OBJECTS) {
			if (mainOne) {
				enlargeNumberOfObjects();
				mainOne = false;
				return AddressBookJavaFxDecorator.getAddressBook(true);
			} else {
				enlargeNumberOfObjects();
				return AddressBookJavaFxDecorator.getAddressBook(false);
			}
		} else {
			return null;
		}
	}

	public void addAdditionalButtons() {

		jbtAdd = new AddButton(this, raf);
		jbtRedo = new RedoButton(this, raf);
		jbtUndo = new UndoButton(this, raf);

		if (eventType.ADD.getDoEvent())
			jpButton.getChildren().add(jbtAdd);
		if (eventType.REDO.getDoEvent())
			jpButton.getChildren().add(jbtRedo);
		if (eventType.UNDO.getDoEvent())
			jpButton.getChildren().add(jbtUndo);

		jbtAdd.setOnAction(ae);
		jbtRedo.setOnAction(ae);
		jbtUndo.setOnAction(ae);

	}

	public static void resetNumberOfObjects() {
		number_of_objects = 0;
	}

	public static void reduceNumberOfObjects() {
		number_of_objects--;
	}

	private static void enlargeNumberOfObjects() {
		number_of_objects++;
	}

	public void actionHandled(ActionEvent e) {
		((Command) e.getSource()).Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}
}

interface Command {
	public void Execute();
}

class CommandButton extends Button implements Command, AddressBookJavaFx1Finals {
	public final static int NAME_SIZE = 32;
	public final static int STREET_SIZE = 32;
	public final static int CITY_SIZE = 20;
	public final static int STATE_SIZE = 2;
	public final static int ZIP_SIZE = 5;
	public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);

	protected AddressBookPane p;
	protected RandomAccessFile raf;

	protected Originator originator;
	protected CareTaker careTaker;

	public CommandButton(AddressBookPane pane, RandomAccessFile r) {
		super();
		p = pane;
		raf = r;

		originator = new Originator();
		careTaker = new CareTaker();
	}

	public void Execute() {
	}

	/** Write a record at the end of the file */
	public void writeAddress() {
		try {
			raf.seek(raf.length());

			FixedLengthStringIO.writeFixedLengthString(p.GetName(), NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetStreet(), STREET_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetCity(), CITY_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetState(), STATE_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetZip(), ZIP_SIZE, raf);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** Read a record at the specified position */
	public void readAddress(long position) throws IOException {
		raf.seek(position);

		String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
		String street = FixedLengthStringIO.readFixedLengthString(STREET_SIZE, raf);
		String city = FixedLengthStringIO.readFixedLengthString(CITY_SIZE, raf);
		String state = FixedLengthStringIO.readFixedLengthString(STATE_SIZE, raf);
		String zip = FixedLengthStringIO.readFixedLengthString(ZIP_SIZE, raf);

		p.SetName(name);
		p.SetStreet(street);
		p.SetCity(city);
		p.SetState(state);
		p.SetZip(zip);
	}
}

class NextButton extends CommandButton {
	public NextButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Next");
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = raf.getFilePointer();
			if (raf.length() == 0) {
				p.SetName("");
				p.SetStreet("");
				p.SetCity("");
				p.SetState("");
				p.SetZip("");
			} else if (currentPosition < raf.length())
				readAddress(currentPosition);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class PreviousButton extends CommandButton {
	public PreviousButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Previous");
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = raf.getFilePointer();
			if (raf.length() == 0) {
				p.SetName("");
				p.SetStreet("");
				p.SetCity("");
				p.SetState("");
				p.SetZip("");
			} else if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
				readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
			else
				;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class LastButton extends CommandButton {
	public LastButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Last");
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = raf.length();
			if (raf.length() == 0) {
				p.SetName("");
				p.SetStreet("");
				p.SetCity("");
				p.SetState("");
				p.SetZip("");
			} else if (lastPosition > 0)
				readAddress(lastPosition - 2 * RECORD_SIZE);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class FirstButton extends CommandButton {
	public FirstButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("First");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0) {
				p.SetName("");
				p.SetStreet("");
				p.SetCity("");
				p.SetState("");
				p.SetZip("");
			} else if (raf.length() > 0)
				readAddress(0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class AddButton extends CommandButton {
	public AddButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Add");
	}

	@Override
	public void Execute() {
		writeAddress();
	}
}

class RedoButton extends CommandButton {
	public RedoButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Redo");
	}

	@Override
	public void Execute() {
		if (careTaker.getIndex() != -1) {
			originator.getState(careTaker.getPrev());

			p.SetName(originator.getJtfName());
			p.SetStreet(originator.getJtfStreet());
			p.SetCity(originator.getJtfCity());
			p.SetState(originator.getJtfState());
			p.SetZip(originator.getJtfZip());

			writeAddress();
		}
	}
}

class UndoButton extends CommandButton {
	public UndoButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Undo");
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = raf.length();
			if (lastPosition > 0)
				readAddress(lastPosition - 2 * RECORD_SIZE);

			originator.setState(p.GetName(), p.GetStreet(), p.GetCity(), p.GetState(), p.GetZip());
			if (!originator.ifEmpty()) {
				careTaker.add(originator.saveStateToMemento());
				try {
					if (raf.length() > 0) {
						raf.setLength(raf.length() - RECORD_SIZE * 2);
						if (raf.length() == 0) {
							p.SetName("");
							p.SetStreet("");
							p.SetCity("");
							p.SetState("");
							p.SetZip("");
						} else {
							readAddress(raf.length() - RECORD_SIZE * 2);
						}
					}
					if (raf.length() == 0) {
						p.SetName("");
						p.SetStreet("");
						p.SetCity("");
						p.SetState("");
						p.SetZip("");
					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//*********************************//
//***** Memento Design Pattern ****//
//*********************************//
class Memento {
	private String jtfName;
	private String jtfStreet;
	private String jtfCity;
	private String jtfState;
	private String jtfZip;

	public Memento(String jtfName, String jtfStreet, String jtfCity, String jtfState, String jtfZip) {
		setJtfName(jtfName);
		setJtfStreet(jtfStreet);
		setJtfCity(jtfCity);
		setJtfState(jtfState);
		setJtfZip(jtfZip);
	}

	public String getJtfName() {
		return jtfName;
	}

	public String getJtfStreet() {
		return jtfStreet;
	}

	public String getJtfCity() {
		return jtfCity;
	}

	public String getJtfState() {
		return jtfState;
	}

	public String getJtfZip() {
		return jtfZip;
	}

	public void setJtfName(String jtfName) {
		this.jtfName = jtfName;
	}

	public void setJtfStreet(String jtfStreet) {
		this.jtfStreet = jtfStreet;
	}

	public void setJtfCity(String jtfCity) {
		this.jtfCity = jtfCity;
	}

	public void setJtfState(String jtfState) {
		this.jtfState = jtfState;
	}

	public void setJtfZip(String jtfZip) {
		this.jtfZip = jtfZip;
	}
}

class Originator {
	private String jtfName;
	private String jtfStreet;
	private String jtfCity;
	private String jtfState;
	private String jtfZip;

	public void setState(String jtfName, String jtfStreet, String jtfCity, String jtfState, String jtfZip) {
		setJtfName(jtfName);
		setJtfStreet(jtfStreet);
		setJtfCity(jtfCity);
		setJtfState(jtfState);
		setJtfZip(jtfZip);
	}

	public void getState(Memento prev) {
		if (prev != null) {
			setJtfName(prev.getJtfName());
			setJtfStreet(prev.getJtfStreet());
			setJtfCity(prev.getJtfCity());
			setJtfState(prev.getJtfState());
			setJtfZip(prev.getJtfZip());
		}
	}

	public Memento saveStateToMemento() {
		return new Memento(getJtfName(), getJtfStreet(), getJtfCity(), getJtfState(), getJtfZip());
	}

	public boolean ifEmpty() {
		return getJtfName().length() == 0 && getJtfStreet().length() == 0 && getJtfCity().length() == 0
				&& getJtfState().length() == 0 && getJtfStreet().length() == 0;
	}

	public void setJtfName(String jtfName) {
		this.jtfName = jtfName;
	}

	public void setJtfStreet(String jtfStreet) {
		this.jtfStreet = jtfStreet;
	}

	public void setJtfCity(String jtfCity) {
		this.jtfCity = jtfCity;
	}

	public void setJtfState(String jtfState) {
		this.jtfState = jtfState;
	}

	public void setJtfZip(String jtfZip) {
		this.jtfZip = jtfZip;
	}

	public String getJtfName() {
		return jtfName;
	}

	public String getJtfStreet() {
		return jtfStreet;
	}

	public String getJtfCity() {
		return jtfCity;
	}

	public String getJtfState() {
		return jtfState;
	}

	public String getJtfZip() {
		return jtfZip;
	}
}

class CareTaker {
	private static List<Memento> mementoList = new ArrayList<Memento>();
	private static int index;

	public CareTaker() {
		index = mementoList.size();
	}

	public void add(Memento state) {
		if (state != null) {
			getMementoList().add(state);
			setIndex(mementoList.size() - 1);
		}
	}

	public Memento getPrev() {
		if (index == -1) {
			return null;
		}

		Memento m = mementoList.get(getIndex());
		mementoList.remove(getIndex());
		setIndex(getIndex() - 1);
		return m;
	}

	public List<Memento> getMementoList() {
		return mementoList;
	}

	public void setMementoList(List<Memento> mementoList) {
		CareTaker.mementoList = mementoList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		CareTaker.index = index;
	}

}
