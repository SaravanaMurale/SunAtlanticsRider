package com.courier.sunatlanticsrider.retrofit;

import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.GetToeknResponse;
import com.courier.sunatlanticsrider.model.LoginAuthResponse;
import com.courier.sunatlanticsrider.model.LoginRequest;
import com.courier.sunatlanticsrider.model.LoginResponse;
import com.courier.sunatlanticsrider.model.MobileNumUpdateRequest;
import com.courier.sunatlanticsrider.model.OrderRequest;
import com.courier.sunatlanticsrider.model.OrderResponseDTO;
import com.courier.sunatlanticsrider.model.PasswordUpdateRequest;
import com.courier.sunatlanticsrider.model.PreviousOrderedResponseDTO;
import com.courier.sunatlanticsrider.model.RegisterRiderRequest;
import com.courier.sunatlanticsrider.model.SavePushNotification;
import com.courier.sunatlanticsrider.model.UserNameUpdateRequest;
import com.courier.sunatlanticsrider.utils.BaseURL;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    //Login
   /* @Headers({"Content-Type:application/json"})
    @POST("get_single_user.php")
    Call<SignedInJSONResponse> getLoginUser(@Body SignInDTO signInDTO);
*/

   /*@GET(BuildConfig.NAJDI_END_POINTS + "products")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<List<ProductListResponse>> getProducts();
*/

    @POST(BaseURL.DOMAIN_NAME + "auth/login")
    @Headers({"Content-Type:application/json"})
    Call<LoginAuthResponse> doCheckLogin(@Body LoginRequest loginRequest);

    @POST(BaseURL.DOMAIN_NAME + "auth/User")
    @Headers({"Content-Type:application/json"})
    Call<LoginResponse> doGetUserDetails(@Header("Authorization") String token);

    /*@Headers({"Content-Type:application/json"})
    // @GET(BaseURL.DOMAIN_NAME+BaseURL.SUB_CHECK)
    @GET(BaseURL.DOMAIN_NAME + "check/{mobileNumber}")
    Call<BaseResponse> checkMobileNumberInServer(@Path("mobileNumber") String mobile);
*/

    /*@GET(BaseURL.DOMAIN_NAME + "deliveryDetails/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<OrderResponseDTO> getMyOrderDetails(@Header("Authorization") String token, @Path("userid") int userid);
*/
    @GET(BaseURL.DOMAIN_NAME + "deliveryDetails/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<OrderResponseDTO> getMyCurrentOrders(@Header("Authorization") String token, @Path("userid") int user_id);

    @GET(BaseURL.DOMAIN_NAME + "orderList/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<PreviousOrderedResponseDTO> getmyPreviousOrders(@Header("Authorization") String token, @Path("userid") int user_id);

    @PUT(BaseURL.DOMAIN_NAME + "updateStatus")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateDeliveryProgressStatus(@Header("Authorization") String token, @Body OrderRequest orderRequest);

    @PUT(BaseURL.DOMAIN_NAME + "updateMobileno")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateMobileNumber(@Header("Authorization") String token, @Body MobileNumUpdateRequest mobileNumUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updateuserName")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateUserName(@Header("Authorization") String token, @Body UserNameUpdateRequest userNameUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updatePassword")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updatePassword(@Header("Authorization") String token, @Body PasswordUpdateRequest passwordUpdateRequest);


    @POST(BaseURL.DOMAIN_NAME + "register")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> registerRider(@Body RegisterRiderRequest registerRiderRequest);

    @POST(BaseURL.DOMAIN_NAME + "pushnotification")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> saveNotificationTokenInServer(@Header("Authorization") String token,@Body SavePushNotification loginResponse);

    @GET(BaseURL.DOMAIN_NAME + "getNotification/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<GetToeknResponse> getPushNotificationToken(@Header("Authorization") String token, @Path("userid") int user_id);

    @GET(BaseURL.DOMAIN_NAME + "firebaseNotification/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> triggerNotificationFromServer(@Header("Authorization") String token,@Path("userid") int user_id);


}
