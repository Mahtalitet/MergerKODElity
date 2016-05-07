import java.io.File;
import java.util.ArrayList;

public class ParserManager {

    private ParsingErrorHandler errorHandler;

    public ParserManager(ParsingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean parseBanchOfFilesToCSV(File[] receivedFileList, File saveDirectory) {

        boolean result;
        File directoryToSaveCsv = returnDirectoryToSaveCsv(saveDirectory);
        System.out.println("Parsing resources to directory: "+directoryToSaveCsv);
        ArrayList<ElementParentResources> parsedResources = new ArrayList<>();
        result = fillingListWithResources(parsedResources, receivedFileList);
        ElementParentResources resourcesWithoutDuplicates = generateCommonResourcesWithoutDuplicates(parsedResources);
        result = new CsvWorker(errorHandler).saveResourcesTo(resourcesWithoutDuplicates, directoryToSaveCsv);
        return result;
    }

    public boolean fillingListWithResources(ArrayList<ElementParentResources> listResources, File[] directories) {

        for (File directory : directories) {

            if (directory.getAbsolutePath().endsWith(MergerUtilityStaticVariables.File.FILE_EXTENSION_STRING)) {
                listResources.add(new StringsParser(errorHandler).getIosResourcesFromFile(directory));

            } else if (directory.getAbsolutePath().endsWith(MergerUtilityStaticVariables.File.FILE_EXTENSION_XML)) {
                listResources.add(new XmlParser(errorHandler).getAndroidResourcesFromFile(directory));
            }
        }

        if (listResources != null) {
            System.out.println("Parsed Resources are "+listResources.size());
            return true;
        } else  {
            return false;
        }
    }

    private static ElementParentResources generateCommonResourcesWithoutDuplicates(ArrayList<ElementParentResources> bunchOfResources) {
        return DuplicateCleaner.getListWithoutDuplicates(bunchOfResources);
    }

    public boolean parseBanchOfFilesFromCSV(File[] receivedFileList, File saveDirectory) {
        boolean result = true;
        File[] directoriesToSaveResources = returnDirectoriesToSaveResources(saveDirectory);
        ElementParentResources[] resources = new CsvWorker(errorHandler).loadResourcesFrom(receivedFileList);

        new XmlParser(errorHandler).saveAndroidResourcesToFile(resources[0], directoriesToSaveResources[0]);
        new XmlParser(errorHandler).saveAndroidResourcesToFile(resources[1], directoriesToSaveResources[1]);
        new StringsParser(errorHandler).saveIosResourcesToFile(resources[2], directoriesToSaveResources[2]);
        new StringsParser(errorHandler).saveIosResourcesToFile(resources[3], directoriesToSaveResources[3]);

        return result;
    }

    private static File[] returnDirectoriesToSaveResources(File saveDirectory) {
        File[] directories = new File[4];
        directories[0] = returnDirectoryWithFile(saveDirectory, MergerUtilityStaticVariables.File.XML_ORIGINAL_FILE_NAME_WITH_EXTENSION);
        directories[1] = returnDirectoryWithFile(saveDirectory, MergerUtilityStaticVariables.File.XML_TRANSLATED_FILE_NAME_WITH_EXTENSION);
        directories[2] = returnDirectoryWithFile(saveDirectory, MergerUtilityStaticVariables.File.STRING_ORIGINAL_FILE_NAME_WITH_EXTENSION);
        directories[3] = returnDirectoryWithFile(saveDirectory, MergerUtilityStaticVariables.File.STRING_TRANSLATED_FILE_NAME_WITH_EXTENSION);
        return directories;
    }

    private static File returnDirectoryToSaveCsv(File saveDirectory) {
        return returnDirectoryWithFile(saveDirectory, MergerUtilityStaticVariables.File.CSV_FILE_NAME_WITH_EXTENSION);
    }


    private static File returnDirectoryWithFile(File directory, String filename) {
        File fullDirectory = new File(directory, filename);
        if (fullDirectory.exists()) fullDirectory.delete();
        return fullDirectory;
    }
}
