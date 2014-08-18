package org.osiam.addons.administration.model.validation;

import java.util.Map;

import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.Extension.Field;
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
            String fieldPath = getFieldPath(urn, fieldKey);
            bindingResult.rejectValue(fieldPath, e.getMessage());
        }
    }

    private String getFieldPath(String urn, String fieldKey) {
        return path + "['" + urn + "']['" + fieldKey + "']";
    }

}
