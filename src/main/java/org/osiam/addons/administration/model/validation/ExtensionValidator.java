package org.osiam.addons.administration.model.validation;

import java.util.Map;

import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.Extension.Field;
import org.osiam.resources.scim.ExtensionFieldType;
import org.springframework.validation.BindingResult;

/**
 * This class is responsible for validating single extension fields.
 */
public class ExtensionValidator {
    private final String path;
    private final Map<String, Extension> extensions;
    private final BindingResult bindingResult;

    public ExtensionValidator(
            String path,
            Map<String, Extension> extensions,
            BindingResult bindingResult) {

        this.path = path;
        this.extensions = extensions;
        this.bindingResult = bindingResult;
    }

    public void validate(String urn, String fieldKey, String fieldValue) {
        final Extension extension = extensions.get(urn);
        final Field field = extension.getFields().get(fieldKey);

        try{
            field.getType().fromString(fieldValue);
        }catch(Exception e){
            final String fieldPath = getFieldPath(urn, fieldKey);
            final String message = getMessage(field.getType(), e);
            final Object[] errorArgs = new Object[]{
                    urn, fieldKey, fieldValue
            };

            bindingResult.rejectValue(fieldPath, message, errorArgs, message);
        }
    }

    private String getMessage(ExtensionFieldType<?> type, Exception e) {
        return "msg.validation.error.extension." + type.getName().toLowerCase();
    }

    private String getFieldPath(String urn, String fieldKey) {
        return path + "['" + urn + "']['" + fieldKey + "']";
    }

}
