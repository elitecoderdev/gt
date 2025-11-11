import json
import xml.etree.ElementTree as ET

def remove_namespace_prefix(tag):
    """Remueve el prefijo del namespace, dejando solo el nombre local"""
    if '}' in tag:
        # Formato {namespace}localname
        return tag.split('}')[1]
    elif ':' in tag:
        # Formato prefix:localname
        return tag.split(':')[1]
    else:
        return tag

def xml_to_dict(element):
    """Convierte un elemento XML a diccionario Python sin prefijos"""
    result = {}
    
    # Procesar atributos
    if element.attrib:
        result['@attributes'] = element.attrib
    
    # Procesar hijos
    children = {}
    text_parts = []
    
    if element.text and element.text.strip():
        text_parts.append(element.text.strip())
    
    for child in element:
        # Remover prefijo del namespace
        tag = remove_namespace_prefix(child.tag)
        
        child_dict = xml_to_dict(child)
        
        # Si el tag ya existe, convertir a lista
        if tag in children:
            if not isinstance(children[tag], list):
                children[tag] = [children[tag]]
            children[tag].append(child_dict)
        else:
            children[tag] = child_dict
    
    if element.tail and element.tail.strip():
        text_parts.append(element.tail.strip())
    
    # Combinar texto y niños
    if text_parts and children:
        result['#text'] = ' '.join(text_parts)
        result.update(children)
    elif text_parts:
        # Solo texto
        return ' '.join(text_parts) if len(text_parts) == 1 else text_parts
    elif children:
        # Solo hijos
        result.update(children)
    else:
        # Elemento vacío
        return None
    
    return result if result else None

def process_xml_file(input_file, output_file):
    """Procesa el XML removiendo SOAP envelope y convirtiendo a JSON sin prefijos"""
    
    with open(input_file, 'r', encoding='utf-8') as f:
        xml_content = f.read()
    
    # Parsear el XML
    try:
        root = ET.fromstring(xml_content)
    except ET.ParseError as e:
        print(f"Error parseando XML: {e}")
        return
    
    # Buscar el elemento Body de SOAP
    soap_body = root.find('.//{http://schemas.xmlsoap.org/soap/envelope/}Body')
    if soap_body is None or len(soap_body) == 0:
        print("No se encontró el elemento DIN dentro del SOAP envelope")
        return
    
    # Obtener el elemento DIN (primer hijo del Body)
    din_element = soap_body[0]
    
    # Convertir a diccionario sin prefijos
    din_dict = {remove_namespace_prefix(din_element.tag): xml_to_dict(din_element)}
    
    # Convertir a JSON
    json_output = json.dumps(din_dict, indent=2, ensure_ascii=False)
    
    # Guardar en archivo JSON
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(json_output)
    
    print(f"Conversión completada. Archivo JSON guardado en: {output_file}")
    print("Los prefijos de namespace han sido removidos.")

if __name__ == "__main__":
    input_file = "DIN_MAS_GRANDE.xml"
    output_file = "DIN_MAS_GRANDE.json"
    process_xml_file(input_file, output_file)



