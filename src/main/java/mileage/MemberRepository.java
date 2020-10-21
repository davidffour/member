package mileage;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MemberRepository extends PagingAndSortingRepository<Member, Long>{

    Optional<Member> findByMemberId(Long orderId);


}