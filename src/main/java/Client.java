import lombok.*;


import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Client implements Serializable {

    private String userName;
    private transient char[] password;
    private String hashPass;

    public Client() {
    }
}
