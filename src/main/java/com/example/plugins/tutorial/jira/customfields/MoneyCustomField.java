package com.example.plugins.tutorial.jira.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Map;


@Scanned
public class MoneyCustomField extends AbstractSingleFieldType<BigDecimal> {
    private static final Logger log = LoggerFactory.getLogger(MoneyCustomField.class);

    public MoneyCustomField(
            @ComponentImport
                    CustomFieldValuePersister customFieldValuePersister,
            @ComponentImport
                    GenericConfigManager genericConfigManager) {
        super(customFieldValuePersister, genericConfigManager);
    }

    @Nonnull
    @Override
    protected PersistenceFieldType getDatabaseType() {
        return PersistenceFieldType.TYPE_LIMITED_TEXT;
    }

    @Nullable
    @Override
    protected Object getDbValueFromObject(final BigDecimal bigDecimal) {
        return getStringFromSingularObject(bigDecimal);
    }

    @Nullable
    @Override
    protected BigDecimal getObjectFromDbValue(@Nonnull final Object databaseValue)
            throws FieldValidationException {

        return getSingularObjectFromString((String) databaseValue);
    }

    public String getStringFromSingularObject(final BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.toString();
    }

    public BigDecimal getSingularObjectFromString(final String s) throws FieldValidationException {
        if (s == null) {
            return null;
        }

        try {
            final BigDecimal decimal = new BigDecimal(s);
            // Check that we don't have too many decimal places
            if (decimal.scale() > 2) {
                throw new FieldValidationException(
                        "maximum of 2 decimal places are allowed.");
            }
            return decimal.setScale(2);
        } catch (NumberFormatException ex) {
            throw new FieldValidationException("Not a valid number.");
        }
    }

    @Override
    public Map<String, Object> getVelocityParameters(final Issue issue,
                                                     final CustomField field,
                                                     final FieldLayoutItem fieldLayoutItem) {

        final Map<String, Object> params = super.getVelocityParameters(issue, field, fieldLayoutItem);

        return params;
    }
}