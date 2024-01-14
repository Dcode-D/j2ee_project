import React from "react";
import {Form, Select, Typography} from "antd";
import { SelectProps } from "antd/lib/select";

interface AddFormSelectProps extends SelectProps<string> {
    title: string;
    name: string;
    error?: string | undefined;
    placeholder?: string;
    disabled?: boolean;
    values: string[];
}

const AddFormSelect: React.FC<AddFormSelectProps> = ({
                                                         title,
                                                         name,
                                                         error,
                                                         placeholder,
                                                         disabled,
                                                         values,
                                                         ...selectProps
                                                     }) => {
    return (
        <Form.Item
            label={title}
            name={name}
            validateStatus={error ? "error" : ""}
            help={error}
        >
            <Select placeholder={placeholder} disabled={disabled} {...selectProps}>
                {values.map((value) => (
                    <Select.Option key={value} value={value}>
                        {value}
                    </Select.Option>
                ))}
            </Select>
        </Form.Item>
    );
};

export default AddFormSelect;
