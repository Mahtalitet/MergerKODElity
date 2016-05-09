import com.opencsv.CSVWriter;

public class MergerUtilityStaticVariables {

    public static class Resource {
        public static final String RESOURCE_PARENT = "resources";
        public static final String RESOURCE_STRING = "string";
        public static final String RESOURCE_STRING_ARRAY = "string-array";
        public static final String RESOURCE_PLURALS = "plurals";

        public static final String ATRIBUTE_ITEM = "item";
        public static final String ATRIBUTE_QUANTITY = "quantity";
        public static final String ATRIBUTE_NAME = "name";
    }

    public static class Csv {

        public static final String CSV_TABLE_TITLE_RESOURCE_KEY = "KEY";
        public static final String CSV_TABLE_TITLE_RESOURCE_VALUE = "OLD_VALUE";
        public static final String CSV_TABLE_TITLE_RESOURCE_NEW_VALUE = "NEW_VALUE";
        public static final String CSV_TABLE_TITLE_RESOURCE_TYPE = "RESOURCE_TYPE";
        public static final String CSV_TABLE_TITLE_RESOURCE_TRANSLATABLE = "IS_TRANSLATABLE";
        public static final String CSV_TABLE_TITLE_OS_TYPE = "OS_TYPE";
        public static final char CSV_TABLE_SEPARATOR = ';';
        public static final char CSV_ATRIBUTE_SEPARATOR = '#';
        public static final char CSV_TABLE_QUOTECHAR = CSVWriter.NO_QUOTE_CHARACTER;
        public static final String CSV_TABLE_HEADERS_COLUMN;
        public static final int CSV_READER_SKIP_LINES = 0;

        static  {
            CSV_TABLE_HEADERS_COLUMN = returnSeparetedCsvRowHeader(CSV_TABLE_TITLE_RESOURCE_TYPE,
                    CSV_TABLE_TITLE_OS_TYPE,
                    CSV_TABLE_TITLE_RESOURCE_KEY,
                    CSV_TABLE_TITLE_RESOURCE_VALUE,
                    CSV_TABLE_TITLE_RESOURCE_NEW_VALUE,
                    CSV_TABLE_TITLE_RESOURCE_TRANSLATABLE);
        }

        public static String returnSeparetedCsvRowHeader(String resource_type,
                                                        String os_type,
                                                        String resource_key,
                                                        String resource_value,
                                                        String new_resource_value,
                                                        String translatable) {
            return resource_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                    os_type + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                    resource_key + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                    resource_value + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                    new_resource_value + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR +
                    translatable + MergerUtilityStaticVariables.Csv.CSV_TABLE_SEPARATOR;

        }
    }

    public static class iOsString {
        public static final char STRING_SEPARATOR = '=';
        public static final char STRING_END_SYMBOL = ';';
        public static final char STRING_QUOTECHAR = '\"';
    }

    public static class File {
        public static final String FILE_ENCODING = "UTF-8";

        public static char FILE_EXTENSIONS_SEPARATOR = '.';
        public static String FILE_EXTENSION_CSV = "csv";
        public static String FILE_EXTENSION_STRING = "strings";
        public static String FILE_EXTENSION_XML = "xml";
        public static String ORIGINAL_FILE_NAME = "original";
        public static String TRANSLATED_FILE_NAME = "translated";
        public static String CSV_FILE_NAME = "translating";

        public static String XML_ORIGINAL_FILE_NAME_WITH_EXTENSION;
        public static String XML_TRANSLATED_FILE_NAME_WITH_EXTENSION;
        public static String STRING_ORIGINAL_FILE_NAME_WITH_EXTENSION;
        public static String STRING_TRANSLATED_FILE_NAME_WITH_EXTENSION;
        public static String CSV_FILE_NAME_WITH_EXTENSION;

        static {
            generateFIleNames(ORIGINAL_FILE_NAME, TRANSLATED_FILE_NAME);
        }

        public static void generateFIleNames(String originalFileName, String translatedFileName) {
            CSV_FILE_NAME_WITH_EXTENSION = returnFileNameWithCsvExtension(CSV_FILE_NAME);
            XML_ORIGINAL_FILE_NAME_WITH_EXTENSION = returnFileNameWithXmlExtension(originalFileName);
            XML_TRANSLATED_FILE_NAME_WITH_EXTENSION = returnFileNameWithXmlExtension(translatedFileName);
            STRING_ORIGINAL_FILE_NAME_WITH_EXTENSION = returnFileNameWithStringExtension(originalFileName);
            STRING_TRANSLATED_FILE_NAME_WITH_EXTENSION = returnFileNameWithStringExtension(translatedFileName);
            System.out.println(CSV_FILE_NAME_WITH_EXTENSION);
            System.out.println(XML_ORIGINAL_FILE_NAME_WITH_EXTENSION);
            System.out.println(XML_TRANSLATED_FILE_NAME_WITH_EXTENSION);
            System.out.println(STRING_ORIGINAL_FILE_NAME_WITH_EXTENSION);
            System.out.println(STRING_TRANSLATED_FILE_NAME_WITH_EXTENSION);
        }

        private static String returnFileNameWithXmlExtension(String fileName) {
            return fileName+
                    String.valueOf(FILE_EXTENSIONS_SEPARATOR)+
                    FILE_EXTENSION_XML;
        }

        private static String returnFileNameWithCsvExtension(String fileName) {
            return fileName+
                    String.valueOf(FILE_EXTENSIONS_SEPARATOR)+
                    FILE_EXTENSION_CSV;
        }

        private static String returnFileNameWithStringExtension(String fileName) {
            return fileName+
                    String.valueOf(FILE_EXTENSIONS_SEPARATOR)+
                    FILE_EXTENSION_STRING;
        }
    }

    public static class Gui {
        public static String MENU_SETTINGS_TITLE ="Settings";
        public static String MENUITEM_MAIN_SETTINGS_TITLE ="Main Settings";
        public static String MENUITEM_RES_TO_CSV_TITLE ="Res To CSV Settings";
        public static String MENUITEM_CSV_TO_RES_TITLE ="CSV To Res Settings";



        public static String APP_NAME = "Merger KODElity";
        public static String APP_VERSION = "v 2.1";

        public static String TEXT_ADD_RESOURCES = "Add Resources";
        public static String TEXT_DELETE_RESOURCES = "Delete Resource(s)";
        public static String TEXT_GENERATE_RESOURCES = "Generate Resources!";
        public static String TEXT_ADD_CSV = "Add CSV";
        public static String TEXT_DELETE_CSV = "Delete CSV";
        public static String TEXT_GENERATE_CSV = "Generate CSV!";

        public static String FILE_CHOOSER_TITLE_FOR_RESOURCES = "Choose Android and iOS resources";
        public static String FILE_CHOOSER_TITLE_FOR_CSV = "Choose CSV files";
        public static String FILE_CHOOSER_TITLE_CSV_DIRECTORY = "Choose directory for CSV file";
        public static String FILE_CHOOSER_EXTENSIONS_TITLE_RESOURCES = "String & XML files";
        public static String FILE_CHOOSER_EXTENSIONS_TITLE_CSV = "CSV files";
    }
}
