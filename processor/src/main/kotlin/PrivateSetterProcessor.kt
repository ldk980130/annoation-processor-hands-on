import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class PrivateSetterProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PrivateSetter::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(PrivateSetter::class.java)

        for (element in elements) {
            element as TypeElement
            when (element.kind) {
                ElementKind.FIELD -> writeForField(element)
                else -> processingEnv.messager.printError("Unsupported element: $element")
            }
        }

        return true
    }

    private fun writeForField(element: TypeElement) {
        val fieldName = element.simpleName.toString()
        val className = element.enclosingElement.simpleName.toString()
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
    }
}
