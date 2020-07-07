
package com.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(name = "XRHotel_ServiceSoap", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
})
public interface XRHotelServiceSoap {


    /**
     * 
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "RoomResource_Query", action = "http://tempuri.org/RoomResource_Query")
    @WebResult(name = "RoomResource_QueryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RoomResource_Query", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomResourceQuery")
    @ResponseWrapper(localName = "RoomResource_QueryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomResourceQueryResponse")
    @Action(input = "http://tempuri.org/RoomResource_Query", output = "http://tempuri.org/XRHotel_ServiceSoap/RoomResource_QueryResponse")
    public String roomResourceQuery(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Tempsta_Set", action = "http://tempuri.org/Tempsta_Set")
    @WebResult(name = "Tempsta_SetResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Tempsta_Set", targetNamespace = "http://tempuri.org/", className = "org.tempuri.TempstaSet")
    @ResponseWrapper(localName = "Tempsta_SetResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.TempstaSetResponse")
    @Action(input = "http://tempuri.org/Tempsta_Set", output = "http://tempuri.org/XRHotel_ServiceSoap/Tempsta_SetResponse")
    public String tempstaSet(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetVipGuests", action = "http://tempuri.org/GetVipGuests")
    @WebResult(name = "GetVipGuestsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetVipGuests", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetVipGuests")
    @ResponseWrapper(localName = "GetVipGuestsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetVipGuestsResponse")
    @Action(input = "http://tempuri.org/GetVipGuests", output = "http://tempuri.org/XRHotel_ServiceSoap/GetVipGuestsResponse")
    public String getVipGuests(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "RoomSta_Querry", action = "http://tempuri.org/RoomSta_Querry")
    @WebResult(name = "RoomSta_QuerryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RoomSta_Querry", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomStaQuerry")
    @ResponseWrapper(localName = "RoomSta_QuerryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomStaQuerryResponse")
    @Action(input = "http://tempuri.org/RoomSta_Querry", output = "http://tempuri.org/XRHotel_ServiceSoap/RoomSta_QuerryResponse")
    public String roomStaQuerry(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Invoice_Binging", action = "http://tempuri.org/Invoice_Binging")
    @WebResult(name = "Invoice_BingingResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Invoice_Binging", targetNamespace = "http://tempuri.org/", className = "org.tempuri.InvoiceBinging")
    @ResponseWrapper(localName = "Invoice_BingingResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.InvoiceBingingResponse")
    @Action(input = "http://tempuri.org/Invoice_Binging", output = "http://tempuri.org/XRHotel_ServiceSoap/Invoice_BingingResponse")
    public String invoiceBinging(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GuestCheckOut_Nobill", action = "http://tempuri.org/GuestCheckOut_Nobill")
    @WebResult(name = "GuestCheckOut_NobillResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GuestCheckOut_Nobill", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestCheckOutNobill")
    @ResponseWrapper(localName = "GuestCheckOut_NobillResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestCheckOutNobillResponse")
    @Action(input = "http://tempuri.org/GuestCheckOut_Nobill", output = "http://tempuri.org/XRHotel_ServiceSoap/GuestCheckOut_NobillResponse")
    public String guestCheckOutNobill(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetReservation_Details", action = "http://tempuri.org/GetReservation_Details")
    @WebResult(name = "GetReservation_DetailsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetReservation_Details", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservationDetails")
    @ResponseWrapper(localName = "GetReservation_DetailsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservationDetailsResponse")
    @Action(input = "http://tempuri.org/GetReservation_Details", output = "http://tempuri.org/XRHotel_ServiceSoap/GetReservation_DetailsResponse")
    public String getReservationDetails(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "DoorcardRecord_Chg", action = "http://tempuri.org/DoorcardRecord_Chg")
    @WebResult(name = "DoorcardRecord_ChgResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "DoorcardRecord_Chg", targetNamespace = "http://tempuri.org/", className = "org.tempuri.DoorcardRecordChg")
    @ResponseWrapper(localName = "DoorcardRecord_ChgResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.DoorcardRecordChgResponse")
    @Action(input = "http://tempuri.org/DoorcardRecord_Chg", output = "http://tempuri.org/XRHotel_ServiceSoap/DoorcardRecord_ChgResponse")
    public String doorcardRecordChg(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ContactUpdate", action = "http://tempuri.org/ContactUpdate")
    @WebResult(name = "ContactUpdateResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ContactUpdate", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ContactUpdate")
    @ResponseWrapper(localName = "ContactUpdateResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ContactUpdateResponse")
    @Action(input = "http://tempuri.org/ContactUpdate", output = "http://tempuri.org/XRHotel_ServiceSoap/ContactUpdateResponse")
    public String contactUpdate(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "PostCharge_New", action = "http://tempuri.org/PostCharge_New")
    @WebResult(name = "PostCharge_NewResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "PostCharge_New", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PostChargeNew")
    @ResponseWrapper(localName = "PostCharge_NewResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PostChargeNewResponse")
    @Action(input = "http://tempuri.org/PostCharge_New", output = "http://tempuri.org/XRHotel_ServiceSoap/PostCharge_NewResponse")
    public String postChargeNew(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetInvoice_New", action = "http://tempuri.org/GetInvoice_New")
    @WebResult(name = "GetInvoice_NewResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetInvoice_New", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInvoiceNew")
    @ResponseWrapper(localName = "GetInvoice_NewResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInvoiceNewResponse")
    @Action(input = "http://tempuri.org/GetInvoice_New", output = "http://tempuri.org/XRHotel_ServiceSoap/GetInvoice_NewResponse")
    public String getInvoiceNew(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "MaterSplit", action = "http://tempuri.org/MaterSplit")
    @WebResult(name = "MaterSplitResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "MaterSplit", targetNamespace = "http://tempuri.org/", className = "org.tempuri.MaterSplit")
    @ResponseWrapper(localName = "MaterSplitResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.MaterSplitResponse")
    @Action(input = "http://tempuri.org/MaterSplit", output = "http://tempuri.org/XRHotel_ServiceSoap/MaterSplitResponse")
    public String materSplit(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GuestRemoteCheckOut1", action = "http://tempuri.org/GuestRemoteCheckOut1")
    @WebResult(name = "GuestRemoteCheckOut1Result", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GuestRemoteCheckOut1", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckOut1")
    @ResponseWrapper(localName = "GuestRemoteCheckOut1Response", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckOut1Response")
    @Action(input = "http://tempuri.org/GuestRemoteCheckOut1", output = "http://tempuri.org/XRHotel_ServiceSoap/GuestRemoteCheckOut1Response")
    public String guestRemoteCheckOut1(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetReservation_Batch", action = "http://tempuri.org/GetReservation_Batch")
    @WebResult(name = "GetReservation_BatchResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetReservation_Batch", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservationBatch")
    @ResponseWrapper(localName = "GetReservation_BatchResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservationBatchResponse")
    @Action(input = "http://tempuri.org/GetReservation_Batch", output = "http://tempuri.org/XRHotel_ServiceSoap/GetReservation_BatchResponse")
    public String getReservationBatch(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetGroupRes_Details", action = "http://tempuri.org/GetGroupRes_Details")
    @WebResult(name = "GetGroupRes_DetailsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetGroupRes_Details", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetGroupResDetails")
    @ResponseWrapper(localName = "GetGroupRes_DetailsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetGroupResDetailsResponse")
    @Action(input = "http://tempuri.org/GetGroupRes_Details", output = "http://tempuri.org/XRHotel_ServiceSoap/GetGroupRes_DetailsResponse")
    public String getGroupResDetails(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GroupRsv_Split", action = "http://tempuri.org/GroupRsv_Split")
    @WebResult(name = "GroupRsv_SplitResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GroupRsv_Split", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GroupRsvSplit")
    @ResponseWrapper(localName = "GroupRsv_SplitResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GroupRsvSplitResponse")
    @Action(input = "http://tempuri.org/GroupRsv_Split", output = "http://tempuri.org/XRHotel_ServiceSoap/GroupRsv_SplitResponse")
    public String groupRsvSplit(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "RoomUpgrade_Free", action = "http://tempuri.org/RoomUpgrade_Free")
    @WebResult(name = "RoomUpgrade_FreeResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RoomUpgrade_Free", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomUpgradeFree")
    @ResponseWrapper(localName = "RoomUpgrade_FreeResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomUpgradeFreeResponse")
    @Action(input = "http://tempuri.org/RoomUpgrade_Free", output = "http://tempuri.org/XRHotel_ServiceSoap/RoomUpgrade_FreeResponse")
    public String roomUpgradeFree(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Subaccnt_Query", action = "http://tempuri.org/Subaccnt_Query")
    @WebResult(name = "Subaccnt_QueryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Subaccnt_Query", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SubaccntQuery")
    @ResponseWrapper(localName = "Subaccnt_QueryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SubaccntQueryResponse")
    @Action(input = "http://tempuri.org/Subaccnt_Query", output = "http://tempuri.org/XRHotel_ServiceSoap/Subaccnt_QueryResponse")
    public String subaccntQuery(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CityLedger_Transfer", action = "http://tempuri.org/CityLedger_Transfer")
    @WebResult(name = "CityLedger_TransferResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "CityLedger_Transfer", targetNamespace = "http://tempuri.org/", className = "org.tempuri.CityLedgerTransfer")
    @ResponseWrapper(localName = "CityLedger_TransferResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.CityLedgerTransferResponse")
    @Action(input = "http://tempuri.org/CityLedger_Transfer", output = "http://tempuri.org/XRHotel_ServiceSoap/CityLedger_TransferResponse")
    public String cityLedgerTransfer(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Upload_BmpSignBill", action = "http://tempuri.org/Upload_BmpSignBill")
    @WebResult(name = "Upload_BmpSignBillResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Upload_BmpSignBill", targetNamespace = "http://tempuri.org/", className = "org.tempuri.UploadBmpSignBill")
    @ResponseWrapper(localName = "Upload_BmpSignBillResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.UploadBmpSignBillResponse")
    @Action(input = "http://tempuri.org/Upload_BmpSignBill", output = "http://tempuri.org/XRHotel_ServiceSoap/Upload_BmpSignBillResponse")
    public String uploadBmpSignBill(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Pay_Online", action = "http://tempuri.org/Pay_Online")
    @WebResult(name = "Pay_OnlineResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Pay_Online", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PayOnline")
    @ResponseWrapper(localName = "Pay_OnlineResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PayOnlineResponse")
    @Action(input = "http://tempuri.org/Pay_Online", output = "http://tempuri.org/XRHotel_ServiceSoap/Pay_OnlineResponse")
    public String payOnline(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Pay_Online_Querry", action = "http://tempuri.org/Pay_Online_Querry")
    @WebResult(name = "Pay_Online_QuerryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Pay_Online_Querry", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PayOnlineQuerry")
    @ResponseWrapper(localName = "Pay_Online_QuerryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PayOnlineQuerryResponse")
    @Action(input = "http://tempuri.org/Pay_Online_Querry", output = "http://tempuri.org/XRHotel_ServiceSoap/Pay_Online_QuerryResponse")
    public String payOnlineQuerry(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetReservation", action = "http://tempuri.org/GetReservation")
    @WebResult(name = "GetReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservation")
    @ResponseWrapper(localName = "GetReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetReservationResponse")
    @Action(input = "http://tempuri.org/GetReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/GetReservationResponse")
    public String getReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     *     修改： GetArrivingReservationImpl  ---》GetArrivingReservation


     */
    @WebMethod(operationName = "GetArrivingReservation", action = "http://tempuri.org/GetArrivingReservation")
    @WebResult(name = "GetArrivingReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetArrivingReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetArrivingReservation")
    @ResponseWrapper(localName = "GetArrivingReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetArrivingReservationResponse")
    @Action(input = "http://tempuri.org/GetArrivingReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/GetArrivingReservationResponse")
    public String getArrivingReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetDepartingReservation", action = "http://tempuri.org/GetDepartingReservation")
    @WebResult(name = "GetDepartingReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetDepartingReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetDepartingReservation")
    @ResponseWrapper(localName = "GetDepartingReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetDepartingReservationResponse")
    @Action(input = "http://tempuri.org/GetDepartingReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/GetDepartingReservationResponse")
    public String getDepartingReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetInHouseReservation", action = "http://tempuri.org/GetInHouseReservation")
    @WebResult(name = "GetInHouseReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetInHouseReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInHouseReservation")
    @ResponseWrapper(localName = "GetInHouseReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInHouseReservationResponse")
    @Action(input = "http://tempuri.org/GetInHouseReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/GetInHouseReservationResponse")
    public String getInHouseReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "AssignRoom", action = "http://tempuri.org/AssignRoom")
    @WebResult(name = "AssignRoomResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AssignRoom", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AssignRoom")
    @ResponseWrapper(localName = "AssignRoomResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AssignRoomResponse")
    @Action(input = "http://tempuri.org/AssignRoom", output = "http://tempuri.org/XRHotel_ServiceSoap/AssignRoomResponse")
    public String assignRoom(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ReleaseRoom", action = "http://tempuri.org/ReleaseRoom")
    @WebResult(name = "ReleaseRoomResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ReleaseRoom", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ReleaseRoom")
    @ResponseWrapper(localName = "ReleaseRoomResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ReleaseRoomResponse")
    @Action(input = "http://tempuri.org/ReleaseRoom", output = "http://tempuri.org/XRHotel_ServiceSoap/ReleaseRoomResponse")
    public String releaseRoom(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GuestRemoteCheckIn", action = "http://tempuri.org/GuestRemoteCheckIn")
    @WebResult(name = "GuestRemoteCheckInResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GuestRemoteCheckIn", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckIn")
    @ResponseWrapper(localName = "GuestRemoteCheckInResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckInResponse")
    @Action(input = "http://tempuri.org/GuestRemoteCheckIn", output = "http://tempuri.org/XRHotel_ServiceSoap/GuestRemoteCheckInResponse")
    public String guestRemoteCheckIn(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GuestRemoteCheckOut", action = "http://tempuri.org/GuestRemoteCheckOut")
    @WebResult(name = "GuestRemoteCheckOutResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GuestRemoteCheckOut", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckOut")
    @ResponseWrapper(localName = "GuestRemoteCheckOutResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GuestRemoteCheckOutResponse")
    @Action(input = "http://tempuri.org/GuestRemoteCheckOut", output = "http://tempuri.org/XRHotel_ServiceSoap/GuestRemoteCheckOutResponse")
    public String guestRemoteCheckOut(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetAvailableRoomFeatures", action = "http://tempuri.org/GetAvailableRoomFeatures")
    @WebResult(name = "GetAvailableRoomFeaturesResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetAvailableRoomFeatures", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetAvailableRoomFeatures")
    @ResponseWrapper(localName = "GetAvailableRoomFeaturesResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetAvailableRoomFeaturesResponse")
    @Action(input = "http://tempuri.org/GetAvailableRoomFeatures", output = "http://tempuri.org/XRHotel_ServiceSoap/GetAvailableRoomFeaturesResponse")
    public String getAvailableRoomFeatures(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetInvoice", action = "http://tempuri.org/GetInvoice")
    @WebResult(name = "GetInvoiceResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetInvoice", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInvoice")
    @ResponseWrapper(localName = "GetInvoiceResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetInvoiceResponse")
    @Action(input = "http://tempuri.org/GetInvoice", output = "http://tempuri.org/XRHotel_ServiceSoap/GetInvoiceResponse")
    public String getInvoice(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetProducts", action = "http://tempuri.org/GetProducts")
    @WebResult(name = "GetProductsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetProducts", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetProducts")
    @ResponseWrapper(localName = "GetProductsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetProductsResponse")
    @Action(input = "http://tempuri.org/GetProducts", output = "http://tempuri.org/XRHotel_ServiceSoap/GetProductsResponse")
    public String getProducts();

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetCompanies", action = "http://tempuri.org/GetCompanies")
    @WebResult(name = "GetCompaniesResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetCompanies", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetCompanies")
    @ResponseWrapper(localName = "GetCompaniesResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetCompaniesResponse")
    @Action(input = "http://tempuri.org/GetCompanies", output = "http://tempuri.org/XRHotel_ServiceSoap/GetCompaniesResponse")
    public String getCompanies(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetCompanies2", action = "http://tempuri.org/GetCompanies2")
    @WebResult(name = "GetCompanies2Result", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetCompanies2", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetCompanies2")
    @ResponseWrapper(localName = "GetCompanies2Response", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetCompanies2Response")
    @Action(input = "http://tempuri.org/GetCompanies2", output = "http://tempuri.org/XRHotel_ServiceSoap/GetCompanies2Response")
    public String getCompanies2(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetGuests", action = "http://tempuri.org/GetGuests")
    @WebResult(name = "GetGuestsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetGuests", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetGuests")
    @ResponseWrapper(localName = "GetGuestsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetGuestsResponse")
    @Action(input = "http://tempuri.org/GetGuests", output = "http://tempuri.org/XRHotel_ServiceSoap/GetGuestsResponse")
    public String getGuests(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "PostCharge", action = "http://tempuri.org/PostCharge")
    @WebResult(name = "PostChargeResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "PostCharge", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PostCharge")
    @ResponseWrapper(localName = "PostChargeResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.PostChargeResponse")
    @Action(input = "http://tempuri.org/PostCharge", output = "http://tempuri.org/XRHotel_ServiceSoap/PostChargeResponse")
    public String postCharge(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetBosbill", action = "http://tempuri.org/GetBosbill")
    @WebResult(name = "GetBosbillResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetBosbill", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetBosbill")
    @ResponseWrapper(localName = "GetBosbillResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetBosbillResponse")
    @Action(input = "http://tempuri.org/GetBosbill", output = "http://tempuri.org/XRHotel_ServiceSoap/GetBosbillResponse")
    public String getBosbill(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "UpdateGusetIDcode", action = "http://tempuri.org/UpdateGusetIDcode")
    @WebResult(name = "UpdateGusetIDcodeResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "UpdateGusetIDcode", targetNamespace = "http://tempuri.org/", className = "org.tempuri.UpdateGusetIDcode")
    @ResponseWrapper(localName = "UpdateGusetIDcodeResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.UpdateGusetIDcodeResponse")
    @Action(input = "http://tempuri.org/UpdateGusetIDcode", output = "http://tempuri.org/XRHotel_ServiceSoap/UpdateGusetIDcodeResponse")
    public String updateGusetIDcode(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Checkroom", action = "http://tempuri.org/Checkroom")
    @WebResult(name = "CheckroomResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Checkroom", targetNamespace = "http://tempuri.org/", className = "org.tempuri.Checkroom")
    @ResponseWrapper(localName = "CheckroomResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.CheckroomResponse")
    @Action(input = "http://tempuri.org/Checkroom", output = "http://tempuri.org/XRHotel_ServiceSoap/CheckroomResponse")
    public String checkroom(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "DictionaryGet", action = "http://tempuri.org/DictionaryGet")
    @WebResult(name = "DictionaryGetResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "DictionaryGet", targetNamespace = "http://tempuri.org/", className = "org.tempuri.DictionaryGet")
    @ResponseWrapper(localName = "DictionaryGetResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.DictionaryGetResponse")
    @Action(input = "http://tempuri.org/DictionaryGet", output = "http://tempuri.org/XRHotel_ServiceSoap/DictionaryGetResponse")
    public String dictionaryGet(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "BlackGuest", action = "http://tempuri.org/BlackGuest")
    @WebResult(name = "BlackGuestResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "BlackGuest", targetNamespace = "http://tempuri.org/", className = "org.tempuri.BlackGuest")
    @ResponseWrapper(localName = "BlackGuestResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.BlackGuestResponse")
    @Action(input = "http://tempuri.org/BlackGuest", output = "http://tempuri.org/XRHotel_ServiceSoap/BlackGuestResponse")
    public String blackGuest();

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SrqsUpdate", action = "http://tempuri.org/SrqsUpdate")
    @WebResult(name = "SrqsUpdateResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "SrqsUpdate", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SrqsUpdate")
    @ResponseWrapper(localName = "SrqsUpdateResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SrqsUpdateResponse")
    @Action(input = "http://tempuri.org/SrqsUpdate", output = "http://tempuri.org/XRHotel_ServiceSoap/SrqsUpdateResponse")
    public String srqsUpdate(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "AccntPosting", action = "http://tempuri.org/AccntPosting")
    @WebResult(name = "AccntPostingResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AccntPosting", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AccntPosting")
    @ResponseWrapper(localName = "AccntPostingResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AccntPostingResponse")
    @Action(input = "http://tempuri.org/AccntPosting", output = "http://tempuri.org/XRHotel_ServiceSoap/AccntPostingResponse")
    public String accntPosting(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "AddGuest", action = "http://tempuri.org/AddGuest")
    @WebResult(name = "AddGuestResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AddGuest", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AddGuest")
    @ResponseWrapper(localName = "AddGuestResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AddGuestResponse")
    @Action(input = "http://tempuri.org/AddGuest", output = "http://tempuri.org/XRHotel_ServiceSoap/AddGuestResponse")
    public String addGuest(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetAccredit", action = "http://tempuri.org/GetAccredit")
    @WebResult(name = "GetAccreditResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetAccredit", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetAccredit")
    @ResponseWrapper(localName = "GetAccreditResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetAccreditResponse")
    @Action(input = "http://tempuri.org/GetAccredit", output = "http://tempuri.org/XRHotel_ServiceSoap/GetAccreditResponse")
    public String getAccredit(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Reservation", action = "http://tempuri.org/Reservation")
    @WebResult(name = "ReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Reservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.Reservation")
    @ResponseWrapper(localName = "ReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ReservationResponse")
    @Action(input = "http://tempuri.org/Reservation", output = "http://tempuri.org/XRHotel_ServiceSoap/ReservationResponse")
    public String reservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "RoomForReservation", action = "http://tempuri.org/RoomForReservation")
    @WebResult(name = "RoomForReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RoomForReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomForReservation")
    @ResponseWrapper(localName = "RoomForReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomForReservationResponse")
    @Action(input = "http://tempuri.org/RoomForReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/RoomForReservationResponse")
    public String roomForReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "RoomPrice_Query", action = "http://tempuri.org/RoomPrice_Query")
    @WebResult(name = "RoomPrice_QueryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RoomPrice_Query", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomPriceQuery")
    @ResponseWrapper(localName = "RoomPrice_QueryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RoomPriceQueryResponse")
    @Action(input = "http://tempuri.org/RoomPrice_Query", output = "http://tempuri.org/XRHotel_ServiceSoap/RoomPrice_QueryResponse")
    public String roomPriceQuery(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Ratecode_Query", action = "http://tempuri.org/Ratecode_Query")
    @WebResult(name = "Ratecode_QueryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Ratecode_Query", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RatecodeQuery")
    @ResponseWrapper(localName = "Ratecode_QueryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.RatecodeQueryResponse")
    @Action(input = "http://tempuri.org/Ratecode_Query", output = "http://tempuri.org/XRHotel_ServiceSoap/Ratecode_QueryResponse")
    public String ratecodeQuery(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Reservation_Chancel", action = "http://tempuri.org/Reservation_Chancel")
    @WebResult(name = "Reservation_ChancelResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Reservation_Chancel", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ReservationChancel")
    @ResponseWrapper(localName = "Reservation_ChancelResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ReservationChancelResponse")
    @Action(input = "http://tempuri.org/Reservation_Chancel", output = "http://tempuri.org/XRHotel_ServiceSoap/Reservation_ChancelResponse")
    public String reservationChancel(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Modify_Dep", action = "http://tempuri.org/Modify_Dep")
    @WebResult(name = "Modify_DepResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Modify_Dep", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ModifyDep")
    @ResponseWrapper(localName = "Modify_DepResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.ModifyDepResponse")
    @Action(input = "http://tempuri.org/Modify_Dep", output = "http://tempuri.org/XRHotel_ServiceSoap/Modify_DepResponse")
    public String modifyDep(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "HrsQuerry", action = "http://tempuri.org/HrsQuerry")
    @WebResult(name = "HrsQuerryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "HrsQuerry", targetNamespace = "http://tempuri.org/", className = "org.tempuri.HrsQuerry")
    @ResponseWrapper(localName = "HrsQuerryResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.HrsQuerryResponse")
    @Action(input = "http://tempuri.org/HrsQuerry", output = "http://tempuri.org/XRHotel_ServiceSoap/HrsQuerryResponse")
    public String hrsQuerry();

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "HrsReservation", action = "http://tempuri.org/HrsReservation")
    @WebResult(name = "HrsReservationResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "HrsReservation", targetNamespace = "http://tempuri.org/", className = "org.tempuri.HrsReservation")
    @ResponseWrapper(localName = "HrsReservationResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.HrsReservationResponse")
    @Action(input = "http://tempuri.org/HrsReservation", output = "http://tempuri.org/XRHotel_ServiceSoap/HrsReservationResponse")
    public String hrsReservation(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

    /**
     *
     * @param requestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "AccntHrsRmrate", action = "http://tempuri.org/AccntHrsRmrate")
    @WebResult(name = "AccntHrsRmrateResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AccntHrsRmrate", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AccntHrsRmrate")
    @ResponseWrapper(localName = "AccntHrsRmrateResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.AccntHrsRmrateResponse")
    @Action(input = "http://tempuri.org/AccntHrsRmrate", output = "http://tempuri.org/XRHotel_ServiceSoap/AccntHrsRmrateResponse")
    public String accntHrsRmrate(
            @WebParam(name = "RequestXML", targetNamespace = "http://tempuri.org/")
                    String requestXML);

}
