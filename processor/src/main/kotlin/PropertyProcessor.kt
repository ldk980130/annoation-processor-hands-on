import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

@AutoService(Processor::class)
class PropertyProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Property::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(Property::class.java)

        for (element in elements) {
            when (element.kind.isVariable) {
                true -> writeForField(element as VariableElement)
                else -> processingEnv.messager.printError("Unsupported element: $element")
            }
        }

        return true
    }

    private fun writeForField(element: VariableElement) {
        val propertyName = element.simpleName.toString()
        val type = element.asType()
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        val className = element.enclosingElement.simpleName.toString()

        val property = PropertySpec.builder(propertyName, type.asTypeName())
            .mutable()
            .build()

        processingEnv.filer.createSourceFile("$packageName.$className").openWriter().use {
            FileSpec.builder(packageName, className)
                .addProperty(property)
                .build()
                .writeTo(it)
        }
    }
}
