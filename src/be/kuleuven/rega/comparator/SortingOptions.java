package be.kuleuven.rega.comparator;

public enum SortingOptions {
	ALPHABETICAL_ASCENDING("Alphabetical - Ascending"), ALPHABETICAL_DESCENDING("Alphabetical - Descending"), FREQUENCY_ASCENDING("Frequency - Ascending"), FREQUENCY_DESCENDING("Frequency - Descending");
	private final String description;
	
	private SortingOptions(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static SortingOptions getOptionByDescription(String description) {
		for(SortingOptions sortingOption:values()) {
			if(sortingOption.getDescription().equals(description)) {
				return sortingOption;
			}
		}
		return null;
	}
}
