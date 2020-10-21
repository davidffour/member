package mileage;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Member_table")
public class Member {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private String phoneNo;
    private String nickname;
    private String memberStatus;

    @PostPersist
    public void onPostPersist(){
        MemberJoined memberJoined = new MemberJoined();
        BeanUtils.copyProperties(this, memberJoined);

        memberJoined.setMemberStatus("READY");
        memberJoined.publishAfterCommit();

        mileage.external.Forfeiture forfeiture = new mileage.external.Forfeiture();
        forfeiture.setId(memberId);
        forfeiture.setRemainPoint(0L);

        System.out.println(forfeiture.getMemberId());

        MemberApplication.applicationContext.getBean(mileage.external.ForfeitureService.class).forfeitHstInsert(forfeiture);

    }

    @PostUpdate
    public void onPostUpdate(){
        MemberStatusChanged memberStatusChanged = new MemberStatusChanged();
        BeanUtils.copyProperties(this, memberStatusChanged);
        memberStatusChanged.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove(){
        MemberWithdrawn memberWithdrawn = new MemberWithdrawn();
        BeanUtils.copyProperties(this, memberWithdrawn);
        memberWithdrawn.setMemberStatus("WITHDRAWAL");
        memberWithdrawn.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
        mileage.external.Point point = new mileage.external.Point();
        // mappings goes here

        point.setMemberId(this.getMemberId());
        point.setMemberStatus("WITHDRAWAL");

        MemberApplication.applicationContext.getBean(mileage.external.PointService.class).forfeit(point, id);

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }




}
