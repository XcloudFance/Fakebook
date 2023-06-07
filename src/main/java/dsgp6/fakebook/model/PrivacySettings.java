package dsgp6.fakebook.model;

public class PrivacySettings {
    public boolean showPhoneNumberToSecondDegreeConnections;
    public boolean showEmailAddressToThirdDegreeConnections;
    public boolean showAddress;
    public boolean showHobbies;
    public boolean showJobs;

    public PrivacySettings() {
        // Initialize the default privacy settings here
        this.showPhoneNumberToSecondDegreeConnections = true;
        this.showEmailAddressToThirdDegreeConnections = true;
        this.showAddress = true;
        this.showHobbies = true;
        this.showJobs = true;
    }

    public boolean isShowPhoneNumberToSecondDegreeConnections() { return showPhoneNumberToSecondDegreeConnections; }

    public void setShowPhoneNumberToSecondDegreeConnections(boolean showPhoneNumberToSecondDegreeConnections) { this.showPhoneNumberToSecondDegreeConnections = showPhoneNumberToSecondDegreeConnections; }

    public boolean isShowEmailAddressToThirdDegreeConnections() { return showEmailAddressToThirdDegreeConnections; }

    public void setShowEmailAddressToThirdDegreeConnections(boolean showEmailAddressToThirdDegreeConnections) { this.showEmailAddressToThirdDegreeConnections = showEmailAddressToThirdDegreeConnections; }

    public boolean isShowAddress() { return showAddress; }

    public void setShowAddress(boolean showAddress) { this.showAddress = showAddress; }

    public boolean isShowHobbies() { return showHobbies; }

    public void setShowHobbies(boolean showHobbies) { this.showHobbies = showHobbies; }

    public boolean isShowJobs() { return showJobs; }

    public void setShowJobs(boolean showJobs) { this.showJobs = showJobs; }
}
