package com.synchron.export;

import com.synchron.model.GoogleDoc;
import com.synchron.model.GoogleDocListWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by AnGo on 31.05.2017.
 */
public class XMLIOHandler {
    /**
     * Сохраняет текущую информацию об Google Doc в указанном файле.
     *
     * @param file
     */
    public static void writeGoogleDocsToXMLFile(File file, List<GoogleDoc> googleDocList) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(GoogleDocListWrapper.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        GoogleDocListWrapper wrapper = new GoogleDocListWrapper();
        wrapper.setGoogleDocList(googleDocList);
        // Маршаллируем и сохраняем XML в файл.
        m.marshal(wrapper, file);
    }

    /**
     * Загружает информацию об Google Doc из указанного файла.
     * Текущая информация об адресатах будет заменена.
     *
     * @param file
     */
    public static List<GoogleDoc> readGoogleDocsFromFile(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(GoogleDocListWrapper.class);
        Unmarshaller um = context.createUnmarshaller();
        // Чтение XML из файла и демаршализация.
        GoogleDocListWrapper wrapper = (GoogleDocListWrapper) um.unmarshal(file);

        return wrapper.getGoogleDocList();
    }
}
