<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <xsd:element name="msregcall">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="operation"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="lookup">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="qualifiers"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="copy">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
                <xsd:group maxOccurs="1" minOccurs="1" ref="dataItem"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="delete">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
                <xsd:element maxOccurs="1" minOccurs="1" ref="dataItemID"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="move">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
                <xsd:group maxOccurs="1" minOccurs="1" ref="dataItem"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="username">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="community">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="dataItemID">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="destination">
        <xsd:simpleType>
            <xsd:restriction base="xsd:string"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="all">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
                <xsd:element maxOccurs="1" minOccurs="0" ref="details"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="some">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
                <xsd:element maxOccurs="unbounded" minOccurs="1" ref="dataItemID"/>
                <xsd:element maxOccurs="1" minOccurs="0" ref="details"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:element name="details">
        <xsd:simpleType>
            <xsd:restriction base="xsd:boolean"/>
        </xsd:simpleType>
    </xsd:element>
    <xsd:element name="upload">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:group maxOccurs="1" minOccurs="1" ref="userID"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:group name="operation">
        <xsd:choice>
            <xsd:element maxOccurs="1" minOccurs="1" ref="lookup"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="copy"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="delete"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="move"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="upload"/>
        </xsd:choice>
    </xsd:group>
    <xsd:group name="userID">
        <xsd:sequence>
            <xsd:element maxOccurs="1" minOccurs="1" ref="username"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="community"/>
        </xsd:sequence>
    </xsd:group>
    <xsd:group name="dataItem">
        <xsd:sequence>
            <xsd:element maxOccurs="1" minOccurs="1" ref="dataItemID"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="destination"/>
        </xsd:sequence>
    </xsd:group>
    <xsd:group name="qualifiers">
        <xsd:choice>
            <xsd:element maxOccurs="1" minOccurs="1" ref="all"/>
            <xsd:element maxOccurs="1" minOccurs="1" ref="some"/>
        </xsd:choice>
    </xsd:group>
</xsd:schema>
