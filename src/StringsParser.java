import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StringsParser {

    private ParsingErrorHandler errorHandler;

    public StringsParser(ParsingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ElementParentResources getIosResourcesFromFile(File fileToParse) {

        File source = fileToParse;

        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, MergerUtilityStaticVariables.File.FILE_ENCODING));

            List<ElementChildString> stringsIos = new ArrayList<ElementChildString>();

            if (!reader.ready()) {
                errorHandler.onErrorEventCreated("File \""+fileToParse.getName()+"\" is empty. First you should delete it!");
                return null;
            }

            int lineCount = 1;
            String line = null;
            while((line = reader.readLine()) != null) {

                if (!line.contains(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_SEPARATOR))) {
                    errorHandler.onErrorEventCreated("Error at line \""
                            +lineCount
                            +"\" in the \""
                            +fileToParse.getName()
                            +"\" file. \nString isn't have \"=\" symbol. \nMaybe, this isn't an iOS resource.\nFirst you should resolve it!");
                    return null;
                }

                String[] lineParts = line.split(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_SEPARATOR));

                String key = getClearKeyOrValue(lineParts[0]);
                System.out.println("Key "+key);
                String value = null;
                if (lineParts.length == 2) {
                    value = getClearKeyOrValue(lineParts[1]);
                } else if (lineParts.length > 2) {
                    value = getClearKeyOrValue(mergeValuePartsWithEquallySymbol(lineParts));
                }
                System.out.println("Value "+value);


                if ((key == null) || (value == null)) {
                    errorHandler.onErrorEventCreated("Error at line \""
                            +lineCount
                            +"\" in the \""
                            +fileToParse.getName()
                            +"\" file. \nMaybe, this isn't an iOS resource.\nFirst you should resolve it!");
                    return null;
                }

                if ((key != null) && (value != null)) stringsIos.add(getStringResource(key, value));
                lineCount++;
            }

            setOsTypeToAllResources(stringsIos);
            ElementParentResources recources = new ElementParentResources(stringsIos);
            return recources;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String mergeValuePartsWithEquallySymbol(String[] valueParts) {
        StringBuilder valueBuilder = new StringBuilder();

        for(int i = 1; i < valueParts.length; i++) {
            valueBuilder.append(valueParts[i]);
            if (i < (valueParts.length-1)) {
                valueBuilder.append(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_SEPARATOR));
            }
        }

        return new String(valueBuilder);
    }

    public void saveIosResourcesToFile(ElementParentResources resources, File fileToSave) {
        if (resources.getStrings() == null) return;

        File destination = fileToSave;
        ArrayList<ElementChildString> iosStrings = (ArrayList<ElementChildString>) resources.getStrings();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, MergerUtilityStaticVariables.File.FILE_ENCODING));

            for (int i = 0; i < iosStrings.size(); i++) {
                writer.write(getFormatedString(iosStrings.get(i)));
                if (i != (iosStrings.size()-1)) writer.newLine();
            }
            writer.close();
            errorHandler.onSuccessEventCreated("\""+fileToSave.getName()+"\" saved at the \""+fileToSave.getParent()+"\" folder.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            errorHandler.onErrorEventCreated("I can't save \""+fileToSave.getName()+"\" to the \""+fileToSave+"\" file. \nPlease, close it before generating!");
            e.printStackTrace();
        }
    }

    private static String getFormatedString(ElementChildString stringRes) {

        StringBuilder resultString = new StringBuilder();

        resultString.append(createKey(stringRes.getKey()));
        resultString.append(" "+String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_SEPARATOR)+" ");
        resultString.append(createValue(stringRes.getValue()));

        return new String(resultString);

    }

    private static String createKey(String key) {
        StringBuilder resultString = new StringBuilder();
        resultString.append(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR));
        resultString.append(key);
        resultString.append(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR));
        return new String(resultString);
    }

    private static String createValue(String value) {
        StringBuilder resultString = new StringBuilder();
        resultString.append(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR));
        resultString.append(value);
        resultString.append(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR)+String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_END_SYMBOL));
        return new String(resultString);
    }

    private static ElementChildString getStringResource(String key, String value) {
        return new ElementChildString(key, value);
    }

    private static void setOsTypeToAllResources(List<ElementChildString> strings) {
        for  (ElementChildString string: strings) {
            string.setTypeOs(ElementChildString.System.IOS);
        }
    }

    private String getClearKeyOrValue(String string) {
//        System.out.println(string);

        String value = string.trim();
        if ((value.startsWith(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR)))
                && (value.endsWith(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR)
                +String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_END_SYMBOL)))) {
            System.out.println("Good value!");
            value = value.substring(1, value.length()-2).trim();

        } else if ((value.startsWith(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR)))
                && (value.endsWith(String.valueOf(MergerUtilityStaticVariables.iOsString.STRING_QUOTECHAR)))) {
            System.out.println("Good key!");
            value = value.substring(1, value.length()-1).trim();
        } else {
            return null;
        }

        return value;
    }
}
