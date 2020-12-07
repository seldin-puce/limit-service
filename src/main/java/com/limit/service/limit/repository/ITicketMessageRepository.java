package com.limit.service.limit.repository;

import com.limit.service.limit.model.TicketMessage;
import com.limit.service.limit.model.TicketMessageKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITicketMessageRepository extends JpaRepository<TicketMessage, TicketMessageKey> {

}
