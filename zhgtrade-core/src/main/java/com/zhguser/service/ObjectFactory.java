
package com.zhguser.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.zhguser.service package. 
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

    private final static QName _FindUserByIdResponse_QNAME = new QName("http://service/", "findUserByIdResponse");
    private final static QName _RegisterResponse_QNAME = new QName("http://service/", "registerResponse");
    private final static QName _GetUserResponse_QNAME = new QName("http://service/", "getUserResponse");
    private final static QName _Login_QNAME = new QName("http://service/", "login");
    private final static QName _GetUserListResponse_QNAME = new QName("http://service/", "getUserListResponse");
    private final static QName _FindUsersResponse_QNAME = new QName("http://service/", "findUsersResponse");
    private final static QName _GetAllUsersResponse_QNAME = new QName("http://service/", "getAllUsersResponse");
    private final static QName _UpdateEmailResponse_QNAME = new QName("http://service/", "updateEmailResponse");
    private final static QName _IsMobileExistsResponse_QNAME = new QName("http://service/", "isMobileExistsResponse");
    private final static QName _IsMobileExists_QNAME = new QName("http://service/", "isMobileExists");
    private final static QName _IsEmailExists_QNAME = new QName("http://service/", "isEmailExists");
    private final static QName _GetUserList_QNAME = new QName("http://service/", "getUserList");
    private final static QName _FindUserByUsernameResponse_QNAME = new QName("http://service/", "findUserByUsernameResponse");
    private final static QName _UpdatePasswordResponse_QNAME = new QName("http://service/", "updatePasswordResponse");
    private final static QName _GetUserCountWithFilterResponse_QNAME = new QName("http://service/", "getUserCountWithFilterResponse");
    private final static QName _UpdateMobile_QNAME = new QName("http://service/", "updateMobile");
    private final static QName _UpdateEmail_QNAME = new QName("http://service/", "updateEmail");
    private final static QName _GetUserCountWithFilter_QNAME = new QName("http://service/", "getUserCountWithFilter");
    private final static QName _UpdateSex_QNAME = new QName("http://service/", "updateSex");
    private final static QName _FindUsers_QNAME = new QName("http://service/", "findUsers");
    private final static QName _UpdatePassword_QNAME = new QName("http://service/", "updatePassword");
    private final static QName _GetUserCount_QNAME = new QName("http://service/", "getUserCount");
    private final static QName _FindUserById_QNAME = new QName("http://service/", "findUserById");
    private final static QName _Register_QNAME = new QName("http://service/", "register");
    private final static QName _GetUserCountResponse_QNAME = new QName("http://service/", "getUserCountResponse");
    private final static QName _UpdateSexResponse_QNAME = new QName("http://service/", "updateSexResponse");
    private final static QName _IsUserNameExists_QNAME = new QName("http://service/", "isUserNameExists");
    private final static QName _IsUserNameExistsResponse_QNAME = new QName("http://service/", "isUserNameExistsResponse");
    private final static QName _UpdateMobileResponse_QNAME = new QName("http://service/", "updateMobileResponse");
    private final static QName _GetAllUsers_QNAME = new QName("http://service/", "getAllUsers");
    private final static QName _IsEmailExistsResponse_QNAME = new QName("http://service/", "isEmailExistsResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://service/", "loginResponse");
    private final static QName _FindUserByUsername_QNAME = new QName("http://service/", "findUserByUsername");
    private final static QName _GetUser_QNAME = new QName("http://service/", "getUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zhguser.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUserCountWithFilter }
     * 
     */
    public GetUserCountWithFilter createGetUserCountWithFilter() {
        return new GetUserCountWithFilter();
    }

    /**
     * Create an instance of {@link UpdateEmail }
     * 
     */
    public UpdateEmail createUpdateEmail() {
        return new UpdateEmail();
    }

    /**
     * Create an instance of {@link UpdateMobile }
     * 
     */
    public UpdateMobile createUpdateMobile() {
        return new UpdateMobile();
    }

    /**
     * Create an instance of {@link GetUserCountWithFilterResponse }
     * 
     */
    public GetUserCountWithFilterResponse createGetUserCountWithFilterResponse() {
        return new GetUserCountWithFilterResponse();
    }

    /**
     * Create an instance of {@link UpdatePasswordResponse }
     * 
     */
    public UpdatePasswordResponse createUpdatePasswordResponse() {
        return new UpdatePasswordResponse();
    }

    /**
     * Create an instance of {@link FindUserByUsernameResponse }
     * 
     */
    public FindUserByUsernameResponse createFindUserByUsernameResponse() {
        return new FindUserByUsernameResponse();
    }

    /**
     * Create an instance of {@link GetUserList }
     * 
     */
    public GetUserList createGetUserList() {
        return new GetUserList();
    }

    /**
     * Create an instance of {@link IsEmailExists }
     * 
     */
    public IsEmailExists createIsEmailExists() {
        return new IsEmailExists();
    }

    /**
     * Create an instance of {@link IsMobileExists }
     * 
     */
    public IsMobileExists createIsMobileExists() {
        return new IsMobileExists();
    }

    /**
     * Create an instance of {@link IsMobileExistsResponse }
     * 
     */
    public IsMobileExistsResponse createIsMobileExistsResponse() {
        return new IsMobileExistsResponse();
    }

    /**
     * Create an instance of {@link UpdateEmailResponse }
     * 
     */
    public UpdateEmailResponse createUpdateEmailResponse() {
        return new UpdateEmailResponse();
    }

    /**
     * Create an instance of {@link GetAllUsersResponse }
     * 
     */
    public GetAllUsersResponse createGetAllUsersResponse() {
        return new GetAllUsersResponse();
    }

    /**
     * Create an instance of {@link FindUsersResponse }
     * 
     */
    public FindUsersResponse createFindUsersResponse() {
        return new FindUsersResponse();
    }

    /**
     * Create an instance of {@link GetUserListResponse }
     * 
     */
    public GetUserListResponse createGetUserListResponse() {
        return new GetUserListResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link FindUserByIdResponse }
     * 
     */
    public FindUserByIdResponse createFindUserByIdResponse() {
        return new FindUserByIdResponse();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link FindUserByUsername }
     * 
     */
    public FindUserByUsername createFindUserByUsername() {
        return new FindUserByUsername();
    }

    /**
     * Create an instance of {@link IsEmailExistsResponse }
     * 
     */
    public IsEmailExistsResponse createIsEmailExistsResponse() {
        return new IsEmailExistsResponse();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link GetAllUsers }
     * 
     */
    public GetAllUsers createGetAllUsers() {
        return new GetAllUsers();
    }

    /**
     * Create an instance of {@link UpdateMobileResponse }
     * 
     */
    public UpdateMobileResponse createUpdateMobileResponse() {
        return new UpdateMobileResponse();
    }

    /**
     * Create an instance of {@link IsUserNameExistsResponse }
     * 
     */
    public IsUserNameExistsResponse createIsUserNameExistsResponse() {
        return new IsUserNameExistsResponse();
    }

    /**
     * Create an instance of {@link IsUserNameExists }
     * 
     */
    public IsUserNameExists createIsUserNameExists() {
        return new IsUserNameExists();
    }

    /**
     * Create an instance of {@link UpdateSexResponse }
     * 
     */
    public UpdateSexResponse createUpdateSexResponse() {
        return new UpdateSexResponse();
    }

    /**
     * Create an instance of {@link GetUserCountResponse }
     * 
     */
    public GetUserCountResponse createGetUserCountResponse() {
        return new GetUserCountResponse();
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link FindUserById }
     * 
     */
    public FindUserById createFindUserById() {
        return new FindUserById();
    }

    /**
     * Create an instance of {@link GetUserCount }
     * 
     */
    public GetUserCount createGetUserCount() {
        return new GetUserCount();
    }

    /**
     * Create an instance of {@link UpdatePassword }
     * 
     */
    public UpdatePassword createUpdatePassword() {
        return new UpdatePassword();
    }

    /**
     * Create an instance of {@link FindUsers }
     * 
     */
    public FindUsers createFindUsers() {
        return new FindUsers();
    }

    /**
     * Create an instance of {@link UpdateSex }
     * 
     */
    public UpdateSex createUpdateSex() {
        return new UpdateSex();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUserByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUserByIdResponse")
    public JAXBElement<FindUserByIdResponse> createFindUserByIdResponse(FindUserByIdResponse value) {
        return new JAXBElement<FindUserByIdResponse>(_FindUserByIdResponse_QNAME, FindUserByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<RegisterResponse>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserResponse")
    public JAXBElement<GetUserResponse> createGetUserResponse(GetUserResponse value) {
        return new JAXBElement<GetUserResponse>(_GetUserResponse_QNAME, GetUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserListResponse")
    public JAXBElement<GetUserListResponse> createGetUserListResponse(GetUserListResponse value) {
        return new JAXBElement<GetUserListResponse>(_GetUserListResponse_QNAME, GetUserListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUsersResponse")
    public JAXBElement<FindUsersResponse> createFindUsersResponse(FindUsersResponse value) {
        return new JAXBElement<FindUsersResponse>(_FindUsersResponse_QNAME, FindUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getAllUsersResponse")
    public JAXBElement<GetAllUsersResponse> createGetAllUsersResponse(GetAllUsersResponse value) {
        return new JAXBElement<GetAllUsersResponse>(_GetAllUsersResponse_QNAME, GetAllUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateEmailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateEmailResponse")
    public JAXBElement<UpdateEmailResponse> createUpdateEmailResponse(UpdateEmailResponse value) {
        return new JAXBElement<UpdateEmailResponse>(_UpdateEmailResponse_QNAME, UpdateEmailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsMobileExistsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isMobileExistsResponse")
    public JAXBElement<IsMobileExistsResponse> createIsMobileExistsResponse(IsMobileExistsResponse value) {
        return new JAXBElement<IsMobileExistsResponse>(_IsMobileExistsResponse_QNAME, IsMobileExistsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsMobileExists }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isMobileExists")
    public JAXBElement<IsMobileExists> createIsMobileExists(IsMobileExists value) {
        return new JAXBElement<IsMobileExists>(_IsMobileExists_QNAME, IsMobileExists.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsEmailExists }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isEmailExists")
    public JAXBElement<IsEmailExists> createIsEmailExists(IsEmailExists value) {
        return new JAXBElement<IsEmailExists>(_IsEmailExists_QNAME, IsEmailExists.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserList")
    public JAXBElement<GetUserList> createGetUserList(GetUserList value) {
        return new JAXBElement<GetUserList>(_GetUserList_QNAME, GetUserList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUserByUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUserByUsernameResponse")
    public JAXBElement<FindUserByUsernameResponse> createFindUserByUsernameResponse(FindUserByUsernameResponse value) {
        return new JAXBElement<FindUserByUsernameResponse>(_FindUserByUsernameResponse_QNAME, FindUserByUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePasswordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updatePasswordResponse")
    public JAXBElement<UpdatePasswordResponse> createUpdatePasswordResponse(UpdatePasswordResponse value) {
        return new JAXBElement<UpdatePasswordResponse>(_UpdatePasswordResponse_QNAME, UpdatePasswordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserCountWithFilterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserCountWithFilterResponse")
    public JAXBElement<GetUserCountWithFilterResponse> createGetUserCountWithFilterResponse(GetUserCountWithFilterResponse value) {
        return new JAXBElement<GetUserCountWithFilterResponse>(_GetUserCountWithFilterResponse_QNAME, GetUserCountWithFilterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateMobile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateMobile")
    public JAXBElement<UpdateMobile> createUpdateMobile(UpdateMobile value) {
        return new JAXBElement<UpdateMobile>(_UpdateMobile_QNAME, UpdateMobile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateEmail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateEmail")
    public JAXBElement<UpdateEmail> createUpdateEmail(UpdateEmail value) {
        return new JAXBElement<UpdateEmail>(_UpdateEmail_QNAME, UpdateEmail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserCountWithFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserCountWithFilter")
    public JAXBElement<GetUserCountWithFilter> createGetUserCountWithFilter(GetUserCountWithFilter value) {
        return new JAXBElement<GetUserCountWithFilter>(_GetUserCountWithFilter_QNAME, GetUserCountWithFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSex }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateSex")
    public JAXBElement<UpdateSex> createUpdateSex(UpdateSex value) {
        return new JAXBElement<UpdateSex>(_UpdateSex_QNAME, UpdateSex.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUsers")
    public JAXBElement<FindUsers> createFindUsers(FindUsers value) {
        return new JAXBElement<FindUsers>(_FindUsers_QNAME, FindUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePassword }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updatePassword")
    public JAXBElement<UpdatePassword> createUpdatePassword(UpdatePassword value) {
        return new JAXBElement<UpdatePassword>(_UpdatePassword_QNAME, UpdatePassword.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserCount")
    public JAXBElement<GetUserCount> createGetUserCount(GetUserCount value) {
        return new JAXBElement<GetUserCount>(_GetUserCount_QNAME, GetUserCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUserById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUserById")
    public JAXBElement<FindUserById> createFindUserById(FindUserById value) {
        return new JAXBElement<FindUserById>(_FindUserById_QNAME, FindUserById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<Register>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUserCountResponse")
    public JAXBElement<GetUserCountResponse> createGetUserCountResponse(GetUserCountResponse value) {
        return new JAXBElement<GetUserCountResponse>(_GetUserCountResponse_QNAME, GetUserCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSexResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateSexResponse")
    public JAXBElement<UpdateSexResponse> createUpdateSexResponse(UpdateSexResponse value) {
        return new JAXBElement<UpdateSexResponse>(_UpdateSexResponse_QNAME, UpdateSexResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsUserNameExists }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isUserNameExists")
    public JAXBElement<IsUserNameExists> createIsUserNameExists(IsUserNameExists value) {
        return new JAXBElement<IsUserNameExists>(_IsUserNameExists_QNAME, IsUserNameExists.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsUserNameExistsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isUserNameExistsResponse")
    public JAXBElement<IsUserNameExistsResponse> createIsUserNameExistsResponse(IsUserNameExistsResponse value) {
        return new JAXBElement<IsUserNameExistsResponse>(_IsUserNameExistsResponse_QNAME, IsUserNameExistsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateMobileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "updateMobileResponse")
    public JAXBElement<UpdateMobileResponse> createUpdateMobileResponse(UpdateMobileResponse value) {
        return new JAXBElement<UpdateMobileResponse>(_UpdateMobileResponse_QNAME, UpdateMobileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getAllUsers")
    public JAXBElement<GetAllUsers> createGetAllUsers(GetAllUsers value) {
        return new JAXBElement<GetAllUsers>(_GetAllUsers_QNAME, GetAllUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsEmailExistsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "isEmailExistsResponse")
    public JAXBElement<IsEmailExistsResponse> createIsEmailExistsResponse(IsEmailExistsResponse value) {
        return new JAXBElement<IsEmailExistsResponse>(_IsEmailExistsResponse_QNAME, IsEmailExistsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUserByUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "findUserByUsername")
    public JAXBElement<FindUserByUsername> createFindUserByUsername(FindUserByUsername value) {
        return new JAXBElement<FindUserByUsername>(_FindUserByUsername_QNAME, FindUserByUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getUser")
    public JAXBElement<GetUser> createGetUser(GetUser value) {
        return new JAXBElement<GetUser>(_GetUser_QNAME, GetUser.class, null, value);
    }

}
