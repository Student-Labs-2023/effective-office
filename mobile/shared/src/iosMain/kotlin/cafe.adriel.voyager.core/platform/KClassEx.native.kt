package cafe.adriel.voyager.core.platform

import kotlin.reflect.KClass

internal actual val KClass<*>.multiplatformName: String? get() = qualifiedName
