# limit-service

You will have to build and launch app manually.
I tried do dockerize it, but had problem with manifest file.


---------------------------------------------------------------------
Endpoints:

/configure [POST]
  Endpoint used to configure stake limit.
  
  Parameters:

    time_duration REQUIRED - Time period in which limiting is being applied (IN SECONDS)
  
    stake_limit REQUIRED - Amount after device should be blocked (sum off all stakes in Time duration)
  
    hot_percentage REQUIRED - Percentage of stake_limit value, after which device should be declared as HOT
  
    restriction_expires REQUIRED - Time after device is automatically unbloocked
    
/checkTicket [POST]
  Endpoint used to check if ticket is being accepted
  
  PAYLOAD : 
  
    id  REQUIRED - unique message identifier
    
    deviceId REQUIRED - unique device identifier
    
    stake REQUIRED - Stake paid in by user
   
  ---------------------------------------------------------------------
  
  
  All TicketMessages, BlockedDevices and Limits are written to the H2 database 'localhost:8080/h2-console',
  but mannaged in memmory (LimitState Service) for better efficiency.
  
  Database use 3 tables:
  
    TICKET_MESSAGE - Information about all ticket messages sent from UNBLOCKED devices.
  
    LIMIT_INFO - Information about all limits that have been applied, their lifetime, and status 'ACTIVE' which indicates 
    activity of LIMIT (true/false)
    
    BLOCKED_DEVICE - Information about all blocked devices trough history,
    Id of the limit that blocked them, Id of the message that went over the limit and status 'ACTIVE' that indtiates
    that device is currently blocked(true)/unblocked(false)
    
    
  
  
  
  

  
  
