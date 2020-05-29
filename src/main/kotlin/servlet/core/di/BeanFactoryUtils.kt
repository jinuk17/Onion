package servlet.core.di

import org.reflections.ReflectionUtils
import servlet.core.annotation.Inject
import java.lang.reflect.Constructor


object BeanFactoryUtils {
    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     *
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     * @param clazz
     * @return
     */
    fun getInjectedConstructor(clazz: Class<*>?): Constructor<*>? {
        val injectedConstructors =
            ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject::class.java))
        return if (injectedConstructors.isEmpty()) {
            null
        } else injectedConstructors.iterator().next()
    }

    /**
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     *
     * @param injectedClazz
     * @param preInstanticateBeans
     * @return
     */
    fun findConcreteClass(injectedClazz: Class<*>, preInstanticateBeans: Set<Class<*>>): Class<*>? {
        if (!injectedClazz.isInterface) {
            return injectedClazz
        }
        for (clazz in preInstanticateBeans) {
            val interfaces: Set<Class<*>> = hashSetOf(*clazz.interfaces)
            if (interfaces.contains(injectedClazz)) {
                return clazz
            }
        }
        throw IllegalStateException(injectedClazz.toString() + "인터페이스를 구현하는 Bean이 존재하지 않는다.")
    }
}