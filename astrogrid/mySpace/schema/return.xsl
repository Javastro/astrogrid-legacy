<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <xsd:element name="msreturn">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="optype"/>
                <xsd:element maxOccurs="1" minOccurs="1" ref="result"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="copy">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="move">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="delete">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="dataID">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="result">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="lookup">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="dataItem">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="container">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="upload">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element maxOccurs="1" minOccurs="1" ref="dataID"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:group name="optype">
        <xsd:all>
            <xsd:element maxOccurs="1" minOccurs="1" ref="copy"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="move"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="delete"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="lookup"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="upload"/>
        </xsd:all>
    </xsd:group>
</xsd:schema>
