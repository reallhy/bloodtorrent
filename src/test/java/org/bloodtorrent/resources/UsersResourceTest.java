package org.bloodtorrent.resources;

import org.bloodtorrent.dto.User;
import org.bloodtorrent.repository.UsersRepository;
import org.bloodtorrent.view.ResultView;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: sds
 * Date: 13. 3. 14
 * Time: 오후 1:56
 * To change this template use File | Settings | File Templates.
 */
public class UsersResourceTest {


    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    private User createNewUser() {
        User user = new User();
        user.setId("bloodtorrent@naver.com");
        user.setPassword("password");
        user.setRole("donor");
        user.setFirstName("Blood");
        user.setLastName("Torrent");
        user.setCellPhone("0123456789");
        user.setGender("male");
        user.setBloodGroup("A+");
        user.setAnonymous(false);
        user.setAddress("BR Mehta Ln");
        user.setCity("New Delhi");
        user.setState("Delhi");
        user.setDistance("10");
        user.setBirthDay("18031980");
        user.setLatitude(17.458418734757736);
        user.setLongitude(78.33536359287109);
        return user;
    }

    @Test
    public void shouldCheckEmailDuplication(){
        UsersRepository usersRepository = mock(UsersRepository.class);
        when(usersRepository.get("bloodtorrent@naver.com")).thenReturn(createNewUser());

        User user = createNewUser();
        UsersResource usersResource = new UsersResource(usersRepository);

        assertThat(usersResource.checkEmailDuplicated(new ArrayList<String>(), user).get(0) ,is("This email address is already taken."));
    }

    @Test
    public void shouldRegistDonorThenReturnResultView() {
        UsersRepository usersRepository = mock(UsersRepository.class);
        UsersResource usersResource = new UsersResource(usersRepository);
        User user = createNewUser();
        ResultView resultView = usersResource.registDonor(user.getFirstName(),user.getLastName(),user.getId(),user.getPassword()
                                    ,user.getPassword(),user.getAddress(),user.getCity(),user.getState(),user.getCellPhone(),user.getBloodGroup()
                                    ,user.getDistance(),user.getGender(),user.getBirthDay(),user.isAnonymous(),user.getLatitude(),user.getLongitude(),"1");

        assertThat(resultView.getTemplateName(), is("/ftl/registrationResult.ftl"));
    }







    private <T> void setDummyString(T t, String property, int num) {
        String dummyText = makeDummyString(num);
        invokeMethod(t, property, dummyText);
    }

    private <T> void setDummyNumericString(T t, String property, int num) {
        String dummyNumericText = makeDummyNumericString(num);
        invokeMethod(t, property, dummyNumericText);
    }

    private <T> void invokeMethod(T t, String property, String dummyText) {
        for (Method method : t.getClass().getMethods()) {
            String methodName = method.getName();
            if(methodName.startsWith("set") && methodName.toLowerCase().endsWith(property.toLowerCase())) {
                try {
                    method.invoke(t, dummyText);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String makeDummyString(int num, String dummyChar) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < num ; i++){
            stringBuilder.append(dummyChar);
        }
        return stringBuilder.toString();
    }

    private String makeDummyString(int num) {
        return makeDummyString(num, "*");
    }

    private String makeDummyNumericString(int num) {
        return makeDummyString(num, "1");
    }

}
