package io.github.wkktoria.mantime.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class LogicAspect {
    private final Timer projectCreateGroupTimer;

    LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Around("execution(* io.github.wkktoria.mantime.logic.ProjectService.createGroup(..))")
    Object aroundProjectGroupCreate(ProceedingJoinPoint joinPoint) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new RuntimeException(throwable);
                }
            }
        });
    }
}
