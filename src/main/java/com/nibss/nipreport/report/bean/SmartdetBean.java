/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nibss.nipreport.report.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;

import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FileData;
import com.nibss.nipreport.data.FileDataResponse;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.data.MessageException;
import com.nibss.nipreport.model.ReportResource;

import static com.nibss.nipreport.model.ReportResource.LIST_SMARTDET;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import static com.opencsv.CSVWriter.NO_ESCAPE_CHARACTER;

import com.nibss.nipreport.model.Smartdet;
import com.nibss.nipreport.model.Smartdet.SmartdetEntry;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.html.View;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * @author elixir
 */
@Path(ReportResource.SMARTDET)
@Named
@SessionScoped
public class SmartdetBean extends AbstractBean {

    private final Map<Integer, FileData> fileDataMap = new HashMap<>();
    //private final Map<Integer, Smartdet> smartdetMap = new HashMap<>();
    AtomicInteger counter = new AtomicInteger();

    public SmartdetBean() {
        super(ReportResource.SMARTDET);
    }

    public static SmartdetBean instance() {
        return instance(SmartdetBean.class);
    }

    @Override
    public AbstractFacade getRepository() {
        return null;
    }

    @RolesAllowed({LIST_SMARTDET})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    @Override
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("smartdetId") Long smartdetId) {
        String roleName = LIST_TRANSACTION;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_TRANSACTION;
                roleName = VIEW_TRANSACTION;
                break;
            }
            default: {
                resourceName = LIST_SMARTDET;
                break;
            }
        }
        return renderView(smartdetId, resourceName, roleName);
    }

    @GET
    @Path("files")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse files() {
        DataResponse dataResponse = new DataResponse();
        List<FileData> data = new ArrayList<>();
        data.addAll(fileDataMap.values());
        dataResponse.setData(data);
        return dataResponse;
    }

    @POST
    @Path("upload-file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse uploadFile(MultipartFormDataInput input, @Context HttpServletRequest request) {
        FileDataResponse dataResponse = new FileDataResponse();
        final Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        if (formDataMap != null) {
            for (Entry<String, List<InputPart>> entry : formDataMap.entrySet()) {
                List<InputPart> c = entry.getValue();
                for (InputPart inputPart : c) {
                    if (inputPart != null) {
                        FileData<Smartdet> fileData = new FileData();
                        try {
                            fileData.setName(dataResponse.getFileName(inputPart.getHeaders()));
                            if (fileData.getName() == null || !fileData.getName().endsWith(".csv")) {
                                throw new MessageException("File must be a Smartdet csv extension");
                            }
                            InputStream stream = inputPart.getBody(InputStream.class, null);
                            byte[] bytes = IOUtils.toByteArray(stream);
                            fileData.setSize(bytes.length);
                            fileData.setData(readSmartdet(bytes));
                            fileData.getData().setName(fileData.getName());
                            // fileData.setSize(dataResponse.saveFile(stream, null));
                        } catch (IOException | MessageException ex) {
                            if (ex instanceof MessageException) {
                                dataResponse.addMessage(((MessageException) ex).message());
                            }
                            fileData.setError(ex.getMessage());
                            Logger.getLogger(SmartdetBean.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            fileData.setDeleteType(HttpMethod.DELETE);
                            dataResponse.addFile(fileData);
                            fileData.setId(counter.incrementAndGet());
                            fileDataMap.put(fileData.getId(), fileData);
                            //smartdetMap.put(fileData.getId(), smartdet);
                        }
                    }
                }
            }
        }
        return dataResponse;
    }

    @DELETE
    @Path("delete-file")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse deleteFile(@QueryParam("fileId") List<Integer> fileIdList) {
        FileDataResponse dataResponse = new FileDataResponse();
        if (fileIdList != null) {
            for (Integer id : fileIdList) {
                if (id != null) {
                    FileData fileData = fileDataMap.get(id);
                    if (fileData != null && fileData.getData() != null) {
                        dataResponse.addData(fileData);
                    }
                }
            }
        } else {
            dataResponse.addMessage(new Message(Message.SEVERITY_ERROR, "No Selection Found"));
        }
        return dataResponse;
    }

    @POST
    @GET
    @Path("add-files")
    @Produces("application/octet-stream")
    public Response addFiles(@QueryParam("fileId") List<Integer> fileIdList, @QueryParam("defaultFileId") Integer defaultFileId) {
        Smartdet smartdet = processSmartdet(fileIdList, defaultFileId, true);
        return smartdet != null ? downloadSmartdet(smartdet) : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @GET
    @Path("subtract-files")
    @Produces("application/octet-stream")
    public Response substractFiles(@QueryParam("fileId") List<Integer> fileIdList, @QueryParam("defaultFileId") Integer defaultFileId) {
        Smartdet smartdet = processSmartdet(fileIdList, defaultFileId, false);
        return smartdet != null ? downloadSmartdet(smartdet) : Response.status(Response.Status.BAD_REQUEST).build();
    }

    public Smartdet processSmartdet(List<Integer> fileIdList, Integer defaultFileId, boolean add) {
        if (fileIdList != null && !fileIdList.isEmpty()) {
            FileData<Smartdet> data = fileDataMap.get(defaultFileId != null ? defaultFileId : fileIdList.get(0));
            Smartdet mainSmartdet = data != null && data.getData() != null ? (Smartdet) data.getData().clone() : null;
            for (Integer id : fileIdList) {
                if (id == null || id.equals(defaultFileId)) {
                    continue;
                }
                FileData<Smartdet> fileData = fileDataMap.get(id);
                if (fileData != null && fileData.getData() != null) {
                    if (mainSmartdet == null) {
                        mainSmartdet = fileData.getData() != null ? (Smartdet) fileData.getData().clone() : null;
                        continue;
                    }
                    for (SmartdetEntry entry : fileData.getData()) {
                        if (entry == null || entry.getAccountNumber() == null) {
                            continue;
                        }
                        for (SmartdetEntry mainEntry : mainSmartdet) {
                            if (mainEntry != null && mainEntry.getAccountNumber() != null && mainEntry.getAccountNumber().equals(entry.getAccountNumber())) {
                                mainEntry.setAmount(add ? mainEntry.getAmount().add(entry.getAmount()) : mainEntry.getAmount().subtract(entry.getAmount()));
                            }
                        }
                    }
                }
            }
            return mainSmartdet;
        }
        return null;
    }

    private Response downloadSmartdet(Smartdet smartdet) {
        try {
            if (smartdet == null) {
                throw new IllegalArgumentException();
            }
            StringWriter sw = new StringWriter();
            CSVWriter writer = new CSVWriter(sw, '\t', CSVWriter.NO_ESCAPE_CHARACTER);
            for (SmartdetEntry entry : smartdet) {
                if (entry != null) {
                    String[] line = new String[]{entry.getProductCode(), entry.getDate(), entry.getAccountNumber(), entry.getAmount().toPlainString()};
                    writer.writeNext(line);
                }
            }
            writer.close();
            sw.close();
            String mimeType = "application/octet-stream";
            byte[] stream = sw.getBuffer().toString().getBytes("UTF-8");
            if (stream == null) {
                throw new IllegalArgumentException();
            }
            String filename = smartdet.getName() != null ? smartdet.getName() + "_" + formatAsFileTimestamp(new Date()) + ".txt" : "smartdet-file.txt";
            return Response.ok(stream, mimeType)
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .cookie(new NewCookie(AppConstant.FILE_DOWNLOAD_COOKIE_NAME, "true", "/", null, null, -1, false))
                    .build();
        } catch (IOException | IllegalArgumentException e) {
            log(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private Smartdet readSmartdet(byte[] bytes) throws MessageException, IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(bytes)), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        Smartdet smartdet = new Smartdet();
        String[] line = null;
        while ((line = reader.readNext()) != null) {
            if (line.length == 0) {
                continue;
            }
            if (line.length < 4) {
                throw new MessageException("One or more line(s) has a shorter column length");
            }
            try {
                SmartdetEntry smartdetEntry = smartdet.new SmartdetEntry();
                smartdetEntry.setProductCode(line[0].trim());
                smartdetEntry.setDate(line[1].trim());
                smartdetEntry.setAccountNumber(line[2].trim());
                smartdetEntry.setAmount(new BigDecimal(line[3].trim()));
                smartdet.add(smartdetEntry);
            } catch (Exception e) {
                throw new MessageException("Propably Amount field cannot be formated", e);
            }
        }
        return smartdet;
    }

    private SimpleDateFormat fileTimestampFormat;

    public String formatAsFileTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        if (fileTimestampFormat == null) {
            fileTimestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            fileTimestampFormat.setLenient(false);
        }
        return fileTimestampFormat.format(date);
    }
}
