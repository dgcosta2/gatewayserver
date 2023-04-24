package com.optimagrowth.gateway.filters;

import org.springframeword.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter{
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    FilterUtils filterUtils;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        if(isCorrelationIdPresent(requestHeaders)){
            logger.debug("tmx=correlation-id found in trackingfilter:{}",
                    filterUtils.getCorrelationId(requestHeaders));

        }
        else{
            String correlationId = generateCorrelationid();
            exchange = filterUtils.setCorrelationID(exchange,correlationId);
            logger.debug("tmx-correlation-id generated in tracking filter {}.",correlationId);
            return chain.filter(exchange);

        }
    }
}