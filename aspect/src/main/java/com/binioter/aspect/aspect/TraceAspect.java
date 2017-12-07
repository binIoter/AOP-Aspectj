package com.binioter.aspect.aspect;

import com.binioter.aspect.annotation.DebugTrace;
import com.binioter.aspect.internal.DebugLog;
import com.binioter.aspect.internal.MethodMsg;
import com.binioter.aspect.internal.StopWatch;
import com.binioter.aspect.util.MLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 截获类名最后含有Activity、Layout的类的所有方法
 * 监听目标方法的执行时间
 */
@Aspect public class TraceAspect {
  private static Object currentObject = null;

  private static final String POINTCUT_METHOD =
      "(execution(* *..Activity+.*(..)) || execution(* *..Layout+.*(..))) && target(Object) && this(Object)";

  private static final String POINTCUT_METHOD_MAINACTIVITY =
      "execution(* *..MainActivity+.onCreate(..))";
  //精确截获MyFrameLayou的onMeasure方法
  private static final String POINTCUT_CALL =
      "call(* org.android10.viewgroupperformance.component.MyFrameLayout.onMeasure(..))";

  @Pointcut(POINTCUT_METHOD) public void methodAnnotated() {
  }

  @Pointcut(POINTCUT_METHOD_MAINACTIVITY) public void methodAnnotatedWith() {
  }

  /**
   * 截获原方法，并替换
   *
   * @throws Throwable
   */
  @Around("methodAnnotated()") @DebugTrace public Object weaveJoinPoint(
      ProceedingJoinPoint joinPoint) throws Throwable {

    if (currentObject == null) {
      currentObject = joinPoint.getTarget();
    }
    //初始化计时器
    final StopWatch stopWatch = new StopWatch();
    //开始监听
    stopWatch.start();
    //调用原方法的执行。
    Object result = joinPoint.proceed();
    //监听结束
    stopWatch.stop();
    //获取方法信息对象
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String className;
    //获取当前对象，通过反射获取类别详细信息
    className = joinPoint.getThis().getClass().getName();

    String methodName = methodSignature.getName();
    String msg = buildLogMessage(methodName, stopWatch.getTotalTime(1));
    if (currentObject != null && currentObject.equals(joinPoint.getTarget())) {
      DebugLog.log(new MethodMsg(className, msg, stopWatch.getTotalTime(1)));
    } else if (currentObject != null && !currentObject.equals(joinPoint.getTarget())) {
      DebugLog.log(new MethodMsg(className, msg, stopWatch.getTotalTime(1)));
      currentObject = joinPoint.getTarget();
    }
    return result;
  }

  /**
   * 创建一个日志信息
   *
   * @param methodName 方法名
   * @param methodDuration 执行时间
   */
  private static String buildLogMessage(String methodName, double methodDuration) {
    StringBuilder message = new StringBuilder();
    message.append(methodName);
    message.append(" --> ");
    message.append("[");
    message.append(methodDuration);
    if (StopWatch.Accuracy == 1) {
      message.append("ms");
    } else {
      message.append("mic");
    }
    message.append("]      \n");

    return message.toString();
  }
}
