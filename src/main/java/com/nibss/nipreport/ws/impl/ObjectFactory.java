
package com.nibss.nipreport.ws.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nibss.nipreport.ws.impl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GenerateSettlementReportResponse_QNAME = new QName("http://com.nibss.nipreport.ws/", "GenerateSettlementReportResponse");
    private final static QName _GenerateSettlementReport_QNAME = new QName("http://com.nibss.nipreport.ws/", "GenerateSettlementReport");
    private final static QName _GenerateBillingReport_QNAME = new QName("http://com.nibss.nipreport.ws/", "GenerateBillingReport");
    private final static QName _CompareSettlementReportResponse_QNAME = new QName("http://com.nibss.nipreport.ws/", "CompareSettlementReportResponse");
    private final static QName _PullExcepionLog_QNAME = new QName("http://com.nibss.nipreport.ws/", "PullExcepionLog");
    private final static QName _PullExcepionLogResponse_QNAME = new QName("http://com.nibss.nipreport.ws/", "PullExcepionLogResponse");
    private final static QName _RegenerateSettlementReport_QNAME = new QName("http://com.nibss.nipreport.ws/", "RegenerateSettlementReport");
    private final static QName _GenerateBillingReportResponse_QNAME = new QName("http://com.nibss.nipreport.ws/", "GenerateBillingReportResponse");
    private final static QName _RegenerateSettlementReportResponse_QNAME = new QName("http://com.nibss.nipreport.ws/", "RegenerateSettlementReportResponse");
    private final static QName _CompareSettlementReport_QNAME = new QName("http://com.nibss.nipreport.ws/", "CompareSettlementReport");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nibss.nipreport.ws.impl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CompareSettlementReport }
     * 
     */
    public CompareSettlementReport createCompareSettlementReport() {
        return new CompareSettlementReport();
    }

    /**
     * Create an instance of {@link GenerateBillingReportResponse }
     * 
     */
    public GenerateBillingReportResponse createGenerateBillingReportResponse() {
        return new GenerateBillingReportResponse();
    }

    /**
     * Create an instance of {@link RegenerateSettlementReportResponse }
     * 
     */
    public RegenerateSettlementReportResponse createRegenerateSettlementReportResponse() {
        return new RegenerateSettlementReportResponse();
    }

    /**
     * Create an instance of {@link GenerateBillingReport }
     * 
     */
    public GenerateBillingReport createGenerateBillingReport() {
        return new GenerateBillingReport();
    }

    /**
     * Create an instance of {@link CompareSettlementReportResponse }
     * 
     */
    public CompareSettlementReportResponse createCompareSettlementReportResponse() {
        return new CompareSettlementReportResponse();
    }

    /**
     * Create an instance of {@link PullExcepionLog }
     * 
     */
    public PullExcepionLog createPullExcepionLog() {
        return new PullExcepionLog();
    }

    /**
     * Create an instance of {@link PullExcepionLogResponse }
     * 
     */
    public PullExcepionLogResponse createPullExcepionLogResponse() {
        return new PullExcepionLogResponse();
    }

    /**
     * Create an instance of {@link RegenerateSettlementReport }
     * 
     */
    public RegenerateSettlementReport createRegenerateSettlementReport() {
        return new RegenerateSettlementReport();
    }

    /**
     * Create an instance of {@link GenerateSettlementReportResponse }
     * 
     */
    public GenerateSettlementReportResponse createGenerateSettlementReportResponse() {
        return new GenerateSettlementReportResponse();
    }

    /**
     * Create an instance of {@link GenerateSettlementReport }
     * 
     */
    public GenerateSettlementReport createGenerateSettlementReport() {
        return new GenerateSettlementReport();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateSettlementReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "GenerateSettlementReportResponse")
    public JAXBElement<GenerateSettlementReportResponse> createGenerateSettlementReportResponse(GenerateSettlementReportResponse value) {
        return new JAXBElement<GenerateSettlementReportResponse>(_GenerateSettlementReportResponse_QNAME, GenerateSettlementReportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateSettlementReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "GenerateSettlementReport")
    public JAXBElement<GenerateSettlementReport> createGenerateSettlementReport(GenerateSettlementReport value) {
        return new JAXBElement<GenerateSettlementReport>(_GenerateSettlementReport_QNAME, GenerateSettlementReport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateBillingReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "GenerateBillingReport")
    public JAXBElement<GenerateBillingReport> createGenerateBillingReport(GenerateBillingReport value) {
        return new JAXBElement<GenerateBillingReport>(_GenerateBillingReport_QNAME, GenerateBillingReport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompareSettlementReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "CompareSettlementReportResponse")
    public JAXBElement<CompareSettlementReportResponse> createCompareSettlementReportResponse(CompareSettlementReportResponse value) {
        return new JAXBElement<CompareSettlementReportResponse>(_CompareSettlementReportResponse_QNAME, CompareSettlementReportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PullExcepionLog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "PullExcepionLog")
    public JAXBElement<PullExcepionLog> createPullExcepionLog(PullExcepionLog value) {
        return new JAXBElement<PullExcepionLog>(_PullExcepionLog_QNAME, PullExcepionLog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PullExcepionLogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "PullExcepionLogResponse")
    public JAXBElement<PullExcepionLogResponse> createPullExcepionLogResponse(PullExcepionLogResponse value) {
        return new JAXBElement<PullExcepionLogResponse>(_PullExcepionLogResponse_QNAME, PullExcepionLogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegenerateSettlementReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "RegenerateSettlementReport")
    public JAXBElement<RegenerateSettlementReport> createRegenerateSettlementReport(RegenerateSettlementReport value) {
        return new JAXBElement<RegenerateSettlementReport>(_RegenerateSettlementReport_QNAME, RegenerateSettlementReport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateBillingReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "GenerateBillingReportResponse")
    public JAXBElement<GenerateBillingReportResponse> createGenerateBillingReportResponse(GenerateBillingReportResponse value) {
        return new JAXBElement<GenerateBillingReportResponse>(_GenerateBillingReportResponse_QNAME, GenerateBillingReportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegenerateSettlementReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "RegenerateSettlementReportResponse")
    public JAXBElement<RegenerateSettlementReportResponse> createRegenerateSettlementReportResponse(RegenerateSettlementReportResponse value) {
        return new JAXBElement<RegenerateSettlementReportResponse>(_RegenerateSettlementReportResponse_QNAME, RegenerateSettlementReportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompareSettlementReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.nibss.nipreport.ws/", name = "CompareSettlementReport")
    public JAXBElement<CompareSettlementReport> createCompareSettlementReport(CompareSettlementReport value) {
        return new JAXBElement<CompareSettlementReport>(_CompareSettlementReport_QNAME, CompareSettlementReport.class, null, value);
    }

}
