package com.oracle.datagrid.activeactive;

import com.oracle.datagrid.activeactive.entity.Customer;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class GenerateSchema {
    public GenerateSchema() {}
    public static void main(String[] args) throws JAXBException, IOException {
        class MySchemaOutputResolver extends SchemaOutputResolver {
            File baseDir = new File("schemas");
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                namespaceUri= "http://oracle.com/datagrid/entity";
                suggestedFileName = "customer.xsd";
                return new StreamResult(new File(baseDir, suggestedFileName));
            }
        }
        JAXBContext context = JAXBContext.newInstance(Customer.class);
        context.generateSchema(new MySchemaOutputResolver());
    }
}