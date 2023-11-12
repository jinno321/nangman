package nc.alright.domain.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter @Setter
//@Table(name = "")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId; //프론트에서는 null로주면 알아서 들어감
    private String storeName;
    private String storePhoneNumber;
    private String storeAddress;
    private float storeLatitude;
    private float storeLongitude;
    //여기 스토어 카테고리 추가
    private String storeImage; //null값 허용



    /*@ManyToOne
    private Host host;*/

    public Store(){
    }
}
