import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;

public class XmlParser {

    private ParsingErrorHandler errorHandler;

    public XmlParser(ParsingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ElementParentResources getAndroidResourcesFromFile(File fileToParse) {
        Serializer deserializer = new Persister();
        File source = fileToParse;
//        System.out.println("Directory is "+source.getAbsolutePath());
        ElementParentResources androidResources = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), MergerUtilityStaticVariables.File.FILE_ENCODING));

            if (!reader.ready()) {
                errorHandler.onErrorEventCreated("File \""+fileToParse.getName()+"\" is empty. First you should delete it!");
                return null;
            }

            androidResources = deserializer.read(ElementParentResources.class, reader);
//            System.out.println(source.toString()+" was deserialized.");

        } catch (Exception e) {
            e.printStackTrace();
             errorHandler.onErrorEventCreated(e.getMessage()+"\nin the \""+fileToParse.getName()+"\" file. First you should resolve it!");
//            System.out.println("Was something bad while read!");
        }

        if (androidResources.getStrings() != null) {
            for  (ElementChildString string : androidResources.getStrings()) {
                string.setTypeOs(ElementChildString.System.ANDROID);
            }
        }

        return androidResources;
    }

    public void saveAndroidResourcesToFile(ElementParentResources resources, File fileToSave) {
        if (!resources.isSomeResources()) return;
        Serializer serializer = new Persister();
        File destination = fileToSave;

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), MergerUtilityStaticVariables.File.FILE_ENCODING));
            serializer.write(resources, writer);
            errorHandler.onSuccessEventCreated("\""+fileToSave.getName()+"\" saved at the \""+fileToSave.getParent()+"\" folder.");
        } catch (Exception e) {
            e.printStackTrace();
            errorHandler.onErrorEventCreated("I can't save \""+fileToSave.getName()+"\" file to the \""+fileToSave.getParent()+"\" folder. \nPlease, close it before generating!");
            System.out.println("Was something bad while save!");
        }
    }
}
