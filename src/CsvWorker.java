import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CsvWorker {

    private ParsingErrorHandler errorHandler;

    public CsvWorker(ParsingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean saveResourcesTo(ElementParentResources allResources, File destinationToSave) {
        ArrayList<String[]> csvResourcesToWrite = new ArrayList<String[]>();

        getHeaderToSaveInCsv(csvResourcesToWrite);
        getStringsToSaveInCsv(allResources, csvResourcesToWrite);
        getStringArraysToSaveInCsv(allResources, csvResourcesToWrite);
        getPluralsToSaveInCsv(allResources, csvResourcesToWrite);

        boolean resultToSave = saveResourcesToCsv(csvResourcesToWrite, destinationToSave);


        return resultToSave;
    }

    public ElementParentResources[] loadResourcesFrom(File[] directories) {

        ElementParentResources xmlResourcesOrigin = new ElementParentResources();
        ElementParentResources xmlResourcesTranslated = new ElementParentResources();
        ElementParentResources stringResourcesOrigin = new ElementParentResources();
        ElementParentResources stringResourcesTranslated = new ElementParentResources();

        for (File directory : directories) {
            ArrayList<String[]> getResources = loadResourcesFromCsv(directory);
            parsingResources(getResources, xmlResourcesOrigin, xmlResourcesTranslated, stringResourcesOrigin, stringResourcesTranslated);
            StringReturner.reset();
            ArrayStringReturner.reset();
            PluralReturner.reset();
        }

        return new ElementParentResources[] {xmlResourcesOrigin, xmlResourcesTranslated, stringResourcesOrigin, stringResourcesTranslated};
    }

    public void parsingResources(ArrayList<String[]> getResources,
                                        ElementParentResources xmlResourcesOrigin,
                                        ElementParentResources xmlResourcesTranslated,
                                        ElementParentResources stringResourcesOrigin,
                                        ElementParentResources stringResourcesTranslated) {

        for (String[] oneResource : getResources) {
            System.out.println("Get resources "+getResources.size());
            String resourceType = oneResource[0];
            String osType = oneResource[1];
            String resourceKey = oneResource[2];
            String resourceOldValue = "";
            String resourceNewValue = "";
            String resourceIsTranslatable = "";

            if (oneResource.length == 4) {
                resourceOldValue = oneResource[3].trim();
            } else if (oneResource.length >= 5) {
                resourceOldValue = oneResource[3].trim();
                resourceNewValue = oneResource[4].trim();
                resourceIsTranslatable = oneResource[5];
            }

            switch (resourceType) {

                case MergerUtilityStaticVariables.Resource.RESOURCE_STRING:
                    StringReturner.get(xmlResourcesOrigin, xmlResourcesTranslated, stringResourcesOrigin, stringResourcesTranslated)
                            .addResource(osType, resourceKey, resourceOldValue, resourceNewValue, resourceIsTranslatable);
                    break;
                case MergerUtilityStaticVariables.Resource.RESOURCE_STRING_ARRAY:
                    ArrayStringReturner.get(xmlResourcesOrigin, xmlResourcesTranslated)
                            .addResource(resourceKey, resourceOldValue, resourceNewValue);
                    break;

                case MergerUtilityStaticVariables.Resource.RESOURCE_PLURALS:
                    PluralReturner.get(xmlResourcesOrigin, xmlResourcesTranslated)
                            .addResource(resourceKey, resourceOldValue, resourceNewValue);
                    break;
            }
        }
    }

    public void getHeaderToSaveInCsv(ArrayList<String[]> listWithResources) {
        listWithResources.add(splitStringBySeparator(MergerUtilityStaticVariables.Csv.CSV_TABLE_HEADERS_COLUMN,
                MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR));
    }

    public CSVWriter getCsvWriter(File directory) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(directory), MergerUtilityStaticVariables.File.FILE_ENCODING),
                    MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR, MergerUtilityStaticVariables.Csv.CSV_TABLE_QUOTECHAR);
        } catch (IOException e) {
            errorHandler.onErrorEventCreated("I can't save \""+directory.getName()+"\" file to the \""+directory.getParent()+"\" folder. \nPlease, close it before generating!");
            e.printStackTrace();
        }

        return writer;
    }

    public CSVReader getCsvReader(File directory) {
        CSVReader reader = null;
        try {
            CSVParser csvParser = new CSVParser(MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER, '\0', CSVParser.DEFAULT_STRICT_QUOTES);
            reader = new CSVReader(new InputStreamReader(new FileInputStream(directory), MergerUtilityStaticVariables.File.FILE_ENCODING),
                    MergerUtilityStaticVariables.Csv.CSV_READER_SKIP_LINES, csvParser);

        } catch (IOException e) {
            errorHandler.onErrorEventCreated("I can't load data from the \""+directory+"\" file. \nPlease, close it before generating!");
            e.printStackTrace();
        }

        return reader;
    }

    public boolean saveResourcesToCsv(ArrayList<String[]> res, File saveDestination) {
        CSVWriter csvWriter = getCsvWriter(saveDestination);
        csvWriter.writeAll(res);
        try {
            csvWriter.close();
            errorHandler.onSuccessEventCreated("\""+saveDestination.getName()+"\" saved at the \""+saveDestination.getParent()+"\" folder.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String[]> loadResourcesFromCsv(File fileDestination) {
        CSVReader csvReader = getCsvReader(fileDestination);
        ArrayList<String[]> res = new ArrayList<String[]>();
        try {
            String[] line = csvReader.readNext();
            if (line != null) {
                if (!Arrays.equals(line, MergerUtilityStaticVariables.Csv.CSV_TABLE_HEADERS_COLUMN.split(String.valueOf(MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR)))) {
                    errorHandler.onErrorEventCreated("File \""+fileDestination.getName()+"\" isn't have good format to generate resources. \nMaybe, first you should generate CSV from resources...");
                    return res;
                } else {
                    while ((line = csvReader.readNext()) != null) {
                        res.add(line);
                    }
                }
            }

            if (res.size() == 0){
                errorHandler.onErrorEventCreated("File \""+fileDestination.getName()+"\" is empty. \nMaybe, first you should generate CSV from resources...");
                return res;
            }


//            res = (ArrayList<String[]>) csvReader.readAll();
            csvReader.close();

//            if (res.size() == 0) {
//                errorHandler.onErrorEventCreated("File \""+fileDestination.getName()+"\" is empty. \nMaybe, first you should generate resources...");
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void getStringsToSaveInCsv(ElementParentResources allResources, ArrayList<String[]> listToWrite) {
        if (allResources.getStrings() == null) return;

        for (ElementChildString string : allResources.getStrings()) {
            String oneRow = returnRowForString(string);
            listToWrite.add(splitStringBySeparator(oneRow, MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR));
        }
    }

    public String returnRowForString(ElementChildString string) {
        String oneString;
        if (string.isTranslatable() == null) {
            if (string.getValue() != null) {
                oneString  = returnSeparetedCsvRow(getResourceType(string),
                        string.getTypeOs().toString(),
                        string.getKey(),
                        string.getValue());
            } else {
                oneString  = returnSeparetedCsvRowWIthoutValue(getResourceType(string),
                        string.getTypeOs().toString(),
                        string.getKey());
            }




        } else {
            oneString = returnSeparetedCsvRowTranslatable(getResourceType(string),
                    string.getTypeOs().toString(),
                    string.getKey(),
                    string.getValue(),
                    string.isTranslatable());
        }

        return oneString;
    }

    public void getStringArraysToSaveInCsv(ElementParentResources allResources, ArrayList<String[]> listToWrite) {
        if (allResources.getStringArrays() == null) return;


        for (ElementChildParentStringArrays arrayString : allResources.getStringArrays()) {

            for (ElementChildItem item : arrayString.getArrayItems()) {
                String oneRow = returnRowForArrayStringOrPlural(getResourceType(arrayString), arrayString.getName(), item);
                listToWrite.add(splitStringBySeparator(oneRow, MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR));
            }
        }
    }

    public void getPluralsToSaveInCsv(ElementParentResources allResources, ArrayList<String[]> listToWrite) {
        if (allResources.getPlurals() == null) return;

        for (ElementChildParentPlurals plural : allResources.getPlurals()) {

            for (ElementChildItem item : plural.getPluralItems()) {
                String oneRow = returnRowForArrayStringOrPlural(getResourceType(plural), plural.getName(), item);
                listToWrite.add(splitStringBySeparator(oneRow, MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR));
            }
        }

    }

    public String returnRowForArrayStringOrPlural(String resourceType, String resourceName, ElementChildItem item) {
        String oneString;

        if (item.getQuantity() == null) {
            oneString = returnSeparetedCsvRow(resourceType,
                    ElementChildString.System.ANDROID.toString(),
                    resourceName,
                    item.getValue());

        } else {
            oneString = returnSeparetedCsvRow(resourceType,
                    ElementChildString.System.ANDROID.toString(),
                    returnKeyToStringArrayOrPlural(resourceName, item, MergerUtilityStaticVariables.Csv.CSV_ATRIBUTE_SEPARATOR),
                    item.getValue());
        }

        return oneString;
    }

    public String returnKeyToStringArrayOrPlural(String resourceName, ElementChildItem item, char separator) {
        return resourceName + String.valueOf(separator) + item.getQuantity();
    }


    public String getResourceType(Object resource) {

        if (resource instanceof ElementChildString) {
            return MergerUtilityStaticVariables.Resource.RESOURCE_STRING;
        } else if (resource instanceof ElementChildParentStringArrays) {
            return MergerUtilityStaticVariables.Resource.RESOURCE_STRING_ARRAY;
        } else if (resource instanceof ElementChildParentPlurals) {
            return MergerUtilityStaticVariables.Resource.RESOURCE_PLURALS;
        }

        return null;
    }

    public final String returnSeparetedCsvRowTranslatable(String resource_type,
                                                                 String os_type,
                                                                 String resource_key,
                                                                 String resource_value,
                                                                 String translatable) {
        return resource_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                os_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                resource_key + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                resource_value + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR+
                MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                translatable + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR;

    }

    public final String returnSeparetedCsvRow(String resource_type,
                                                     String os_type,
                                                     String resource_key,
                                                     String resource_value) {
        return resource_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                os_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                resource_key + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                resource_value + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR;

    }

    public final String returnSeparetedCsvRowWIthoutValue(String resource_type,
                                                     String os_type,
                                                     String resource_key) {
        return resource_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                os_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                resource_key + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR;

    }

    public final String[] splitStringBySeparator(String stringToSplit, char separator) {
        return stringToSplit.split(String.valueOf(separator));
    }


    private static class PluralReturner {

        private ElementParentResources mXmlResourcesOrigin;
        private ElementParentResources mXmlResourcesTranslated;

        private ElementChildParentPlurals mOriginalPlural;
        private ElementChildParentPlurals mTranslatedPlural;

        private static PluralReturner pluralsReturner;

        private PluralReturner(ElementParentResources mXmlResourcesOrigin, ElementParentResources mXmlResourcesTranslated) {
            this.mXmlResourcesOrigin = mXmlResourcesOrigin;
            this.mXmlResourcesTranslated = mXmlResourcesTranslated;
        }

        public static PluralReturner get(ElementParentResources mXmlResourcesOrigin, ElementParentResources mXmlResourcesTranslated) {
            if (pluralsReturner == null) pluralsReturner = new PluralReturner(mXmlResourcesOrigin, mXmlResourcesTranslated);

            return pluralsReturner;
        }

        public void addResource(String resourceKey, String resourceOldValue, String resourceNewValue) {
            String[] pluralKeys = resourceKey.split(String.valueOf(MergerUtilityStaticVariables.Csv.CSV_ATRIBUTE_SEPARATOR));

            if (!resourceNewValue.equals("") ) {
                if (mOriginalPlural == null) {
                    mOriginalPlural = new ElementChildParentPlurals(pluralKeys[0]);
                    mXmlResourcesOrigin.addPluralResource(mOriginalPlural);
                }

                if (mOriginalPlural != null) {
                    if (!mOriginalPlural.getName().equals(pluralKeys[0])) {
                        mOriginalPlural = new ElementChildParentPlurals(pluralKeys[0]);
                        mXmlResourcesOrigin.addPluralResource(mOriginalPlural);
                        mOriginalPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceOldValue));
                    } else {
                        mOriginalPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceOldValue));
                    }
                }


                if (mTranslatedPlural == null) {
                    mTranslatedPlural = new ElementChildParentPlurals(pluralKeys[0]);
                    mXmlResourcesTranslated.addPluralResource(mTranslatedPlural);
                }

                if (mTranslatedPlural != null) {
                    if (!mTranslatedPlural.getName().equals(pluralKeys[0])) {
                        mTranslatedPlural = new ElementChildParentPlurals(pluralKeys[0]);
                        mXmlResourcesTranslated.addPluralResource(mTranslatedPlural);
                        mTranslatedPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceNewValue));
                    } else {
                        mTranslatedPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceNewValue));
                    }
                }

            } else {
                if (mOriginalPlural == null) {
                    mOriginalPlural = new ElementChildParentPlurals(pluralKeys[0]);
                    mXmlResourcesOrigin.addPluralResource(mOriginalPlural);
                }

                if (mOriginalPlural != null) {
                    if (!mOriginalPlural.getName().equals(pluralKeys[0])) {
                        mOriginalPlural = new ElementChildParentPlurals(pluralKeys[0]);
                        mXmlResourcesOrigin.addPluralResource(mOriginalPlural);
                        mOriginalPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceOldValue));
                    } else {
                        mOriginalPlural.addPluralItem(new ElementChildItem(pluralKeys[1], resourceOldValue));
                    }
                }
            }
        }

        public static void reset() {
            pluralsReturner = null;
        }

    }

    private static class StringReturner {
        private ElementParentResources mXmlResourcesOrigin;
        private ElementParentResources mXmlResourcesTranslated;
        private ElementParentResources mStringResourcesOrigin;
        private ElementParentResources mStringResourcesTranslated;

        private ElementChildString mString;
        private ElementChildString mTranslatedString;

        private static StringReturner stringReturner;


        private StringReturner(ElementParentResources mXmlResourcesOrigin,
                               ElementParentResources mXmlResourcesTranslated,
                               ElementParentResources mStringResourcesOrigin,
                               ElementParentResources mStringResourcesTranslated) {
            this.mXmlResourcesOrigin = mXmlResourcesOrigin;
            this.mXmlResourcesTranslated = mXmlResourcesTranslated;
            this.mStringResourcesOrigin = mStringResourcesOrigin;
            this.mStringResourcesTranslated = mStringResourcesTranslated;
        }

        public static StringReturner get(ElementParentResources mXmlResourcesOrigin,
                                         ElementParentResources mXmlResourcesTranslated,
                                         ElementParentResources mStringResourcesOrigin,
                                         ElementParentResources mStringResourcesTranslated) {
            if (stringReturner == null) stringReturner = new StringReturner(mXmlResourcesOrigin, mXmlResourcesTranslated, mStringResourcesOrigin, mStringResourcesTranslated);
            return stringReturner;
        }

        public void addResource(String osType, String resourceKey, String resourceOldValue, String resourceNewValue, String resourceIsTranslatable) {
            mString = new ElementChildString();
            mTranslatedString = new ElementChildString();
            mString.setKey(resourceKey);
            mTranslatedString.setKey(resourceKey);

            if (resourceIsTranslatable.equals("false")) {
                mString.setTranslatable("false");
            }

            if (osType.equals(ElementChildString.System.ANDROID.toString())) {

                if ((resourceNewValue.equals("")) ||
                        (resourceIsTranslatable.equals("false"))) {
                    mString.setValue(resourceOldValue);
                    mXmlResourcesOrigin.addStringResource(mString);
                } else {
                    mString.setValue(resourceOldValue);
                    mTranslatedString.setValue(resourceNewValue);
                    mXmlResourcesOrigin.addStringResource(mString);
                    mXmlResourcesTranslated.addStringResource(mTranslatedString);
                }

            } else if (osType.equals(ElementChildString.System.IOS.toString())) {
                if (resourceNewValue.equals("")) {
                    mString.setValue(resourceOldValue);
                    mStringResourcesOrigin.addStringResource(mString);

                }
                if (!resourceNewValue.equals("")){
                    mString.setValue(resourceOldValue);
                    mTranslatedString.setValue(resourceNewValue);
                    mStringResourcesOrigin.addStringResource(mString);
                    mStringResourcesTranslated.addStringResource(mTranslatedString);
                }

            } else if (osType.equals(ElementChildString.System.BOTH.toString())) {
                if ((resourceNewValue.equals("")) ||
                        (resourceIsTranslatable.equals("false"))) {
                    mString.setValue(resourceOldValue);
                    mXmlResourcesOrigin.addStringResource(mString);
                    mStringResourcesOrigin.addStringResource(mString);

                } else {
                    mString.setValue(resourceOldValue);
                    mTranslatedString.setValue(resourceNewValue);
                    mXmlResourcesOrigin.addStringResource(mString);
                    mXmlResourcesTranslated.addStringResource(mTranslatedString);
                    mStringResourcesOrigin.addStringResource(mString);
                    mStringResourcesTranslated.addStringResource(mTranslatedString);
                }
            }
        }

        public static void reset () {
            stringReturner = null;
        }
    }

    private static class ArrayStringReturner {

        private ElementParentResources mXmlResourcesOrigin;
        private ElementParentResources mXmlResourcesTranslated;

        private ElementChildParentStringArrays mOriginalStringArray;
        private ElementChildParentStringArrays mTranslatedStringArray;

        private static ArrayStringReturner arrayStringReturner;

        private ArrayStringReturner(ElementParentResources mXmlResourcesOrigin, ElementParentResources mXmlResourcesTranslated) {
            this.mXmlResourcesOrigin = mXmlResourcesOrigin;
            this.mXmlResourcesTranslated = mXmlResourcesTranslated;
        }

        public static ArrayStringReturner get(ElementParentResources mXmlResourcesOrigin, ElementParentResources mXmlResourcesTranslated) {
            if (arrayStringReturner == null) arrayStringReturner = new ArrayStringReturner(mXmlResourcesOrigin, mXmlResourcesTranslated);

            return arrayStringReturner;
        }

        public void addResource(String resourceKey, String resourceOldValue, String resourceNewValue) {

            if (!resourceNewValue.equals("") ) {
                if (mOriginalStringArray == null) {
                    mOriginalStringArray = new ElementChildParentStringArrays(resourceKey);
                    mXmlResourcesOrigin.addStringArrayResource(mOriginalStringArray);
                }

                if (mOriginalStringArray != null) {
                    if (!mOriginalStringArray.getName().equals(resourceKey)) {
                        mOriginalStringArray = new ElementChildParentStringArrays(resourceKey);
                        mXmlResourcesOrigin.addStringArrayResource(mOriginalStringArray);
                        mOriginalStringArray.addStringArrayItem(new ElementChildItem(resourceOldValue));
                    } else {
                        mOriginalStringArray.addStringArrayItem(new ElementChildItem(resourceOldValue));
                    }
                }


                if (mTranslatedStringArray == null) {
                    mTranslatedStringArray = new ElementChildParentStringArrays(resourceKey);
                    mXmlResourcesTranslated.addStringArrayResource(mTranslatedStringArray);
                }

                if (mTranslatedStringArray != null) {
                    if (!mTranslatedStringArray.getName().equals(resourceKey)) {
                        mTranslatedStringArray = new ElementChildParentStringArrays(resourceKey);
                        mXmlResourcesTranslated.addStringArrayResource(mTranslatedStringArray);
                        mTranslatedStringArray.addStringArrayItem(new ElementChildItem(resourceNewValue));
                    } else {
                        mTranslatedStringArray.addStringArrayItem(new ElementChildItem(resourceNewValue));
                    }
                }

            } else {
                if (mOriginalStringArray == null) {
                    mOriginalStringArray = new ElementChildParentStringArrays(resourceKey);
                    mXmlResourcesOrigin.addStringArrayResource(mOriginalStringArray);
                }

                if (mOriginalStringArray != null) {
                    if (!mOriginalStringArray.getName().equals(resourceKey)) {
                        mOriginalStringArray = new ElementChildParentStringArrays(resourceKey);
                        mXmlResourcesOrigin.addStringArrayResource(mOriginalStringArray);
                        mOriginalStringArray.addStringArrayItem(new ElementChildItem(resourceOldValue));
                    } else {
                        mOriginalStringArray.addStringArrayItem(new ElementChildItem(resourceOldValue));
                    }
                }
            }
        }

        public static void reset() {
            arrayStringReturner = null;
        }
    }

}