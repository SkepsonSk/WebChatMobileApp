package pl.jakub.chatapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * Class storing the information
 * about user. The format of
 * the class is experimental.
 *
 * @author Jakub Zelmanowicz
 */
public class UserViewModel extends AndroidViewModel {

    private final MutableLiveData<String> uuid;
    private final MutableLiveData<String> username;

    public UserViewModel(Application application) {
        super(application);

        this.uuid = new MutableLiveData<>();
        uuid.setValue("");

        this.username = new MutableLiveData<>();
        username.setValue("");
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getUuid() {
        return uuid;
    }

    public void changeUsername(String username) {
        this.username.postValue(username);
    }

    public void changeUuid(String username) {
        this.uuid.postValue(username);
    }

}
