
public class AddressBookJavaFxDecorator extends AddressBookPane implements AddressBookJavaFx1Finals {

	public static AddressBookPane getAddressBook(boolean paneType) {
		if (paneType) {
			AddressBookPane withAddedButtons = new AddressBookPane();
			withAddedButtons.addAdditionalButtons();
			return withAddedButtons;
		} else {
			return new AddressBookPane();
		}
	}
}
